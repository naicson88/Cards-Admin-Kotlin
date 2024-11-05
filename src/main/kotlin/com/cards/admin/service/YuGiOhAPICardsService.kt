package com.cards.admin.service

import cardscommons.dto.CardYuGiOhAPI
import cardscommons.dto.RelDeckCardsDTO
import com.cards.admin.client.YuGiOhAPIClient
import com.cards.admin.restTemplate.YuGiOhAPICardsRestTemplate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.NoSuchElementException

@Service
class YuGiOhAPICardsService(
        val apiCardsRest : YuGiOhAPICardsRestTemplate,
        val client : YuGiOhAPIClient
) {
    var SET_NAME = ""
    var logger: Logger = LoggerFactory.getLogger(YuGiOhAPICardsService::class.java)

    fun  getCardOnYuGiOhAPI(cardNumber : Long) : CardYuGiOhAPI {
        val json : String? = apiCardsRest.getCardFromYuGiOhAPI(cardNumber)
        require(json != null) {"Cannot get card with number: $cardNumber" }

        return transformJsonInCardObject(json)

    }

    private fun transformJsonInCardObject(json: String) : CardYuGiOhAPI {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)

        val tree = mapper.readTree(json)

        val node = tree.path("data")
        val cardArray: Array<CardYuGiOhAPI> = mapper.readValue(node.toString(), Array<CardYuGiOhAPI>::class.java)

        require( cardArray.size == 1){"It was not possible transform Json in a Card: Invalid array size"}

       return cardArray[0]
    }

    fun getCardsOfADeck(setName: String?) : List<RelDeckCardsDTO> {
        require(!setName.isNullOrBlank()) {"Set Name informed is invalid."}
        this.SET_NAME = setName
        val json : String = client.getCardsFromSet(setName) ?: throw NoSuchElementException("JSON with deck info was empty")

        return this.convertJsonIntoListOfRelDeckCards(json) ?: throw NoSuchElementException("Cards of this Set were not found")
    }

    fun convertJsonIntoListOfRelDeckCards(json: String) : List<RelDeckCardsDTO> ?{
        require(json.isNotBlank()) {"#convertJsonIntoListOfRelDeckCards: JSON with deck info was empty"}

        val obj = JSONObject(json)
        val jsonArray = obj.getJSONArray("data")
        val listRelation = LinkedList<RelDeckCardsDTO>()

        jsonArray.forEachIndexed { i, _ ->
            val card : JSONObject = jsonArray.getJSONObject(i)
            val relation = returnARelDeckCardFromAJSONObject(card)

            if(relation.isNotEmpty())
                relation.forEach { listRelation.add(it) }
        }

        return listRelation

    }

    private fun returnARelDeckCardFromAJSONObject(card: JSONObject): List<RelDeckCardsDTO> {
        val listRelDeckCards = IntStream
                .range(0, card.getJSONArray("card_sets").length())
                .mapToObj<JSONObject> { i: Int -> card.getJSONArray("card_sets").getJSONObject(i) }
                .filter { c: JSONObject -> c["set_name"] == this.SET_NAME }
                .filter { c: JSONObject -> if ((c["set_code"] as String).contains("EN")) (c["set_code"]
                        as String).contains("EN") else true
                }
                .map<RelDeckCardsDTO> { relation: JSONObject ->
                    val relDeckCards = RelDeckCardsDTO()
                    relDeckCards.cardNumber = Integer.toUnsignedLong((card["id"] as Int))
                    relDeckCards.card_price = (relation["set_price"] as String).toDouble()
                    relDeckCards.card_raridade = relation["set_rarity"] as String
                    relDeckCards.setRarityCode = relation["set_rarity_code"] as String
                    relDeckCards.rarityDetails = relation["set_rarity"] as String
                    relDeckCards.cardSetCode = relation["set_code"] as String
                    relDeckCards.dt_criacao = Date()
                    relDeckCards.isSideDeck = false
                    relDeckCards
                }.distinct()
                .collect(Collectors.toList<RelDeckCardsDTO>())

        return this.editSetCodeDuplicated(listRelDeckCards)
    }

    private fun editSetCodeDuplicated(listRelDeckCards: List<RelDeckCardsDTO>) : List<RelDeckCardsDTO> {
        val mapDuplicated : Map<String, Int> = listRelDeckCards
                .groupingBy { it.cardSetCode }
                .eachCount()
                .filter { it.value > 1 }

        for(map in mapDuplicated) {
            listRelDeckCards.filter { it.cardSetCode == map.key}.forEach { it.cardSetCode += it.setRarityCode }
        }

        return listRelDeckCards
    }
}