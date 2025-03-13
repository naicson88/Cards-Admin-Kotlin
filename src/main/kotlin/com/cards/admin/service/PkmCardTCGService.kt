package com.cards.admin.service

import cardscommons.dto.CardYuGiOhAPI
import com.cards.admin.client.PkmCardTCGApi
import com.cards.admin.client.PkmDjangoClient
import com.cards.admin.dto.PkmAttackDjango
import com.cards.admin.dto.PkmCardDTODjango
import com.cards.admin.dto.PkmCardTCGDto
import com.cards.admin.enums.PkmCardEnergyType
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import okhttp3.internal.wait
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap


@Service
class PkmCardTCGService(val client: PkmCardTCGApi, val djangoClient: PkmDjangoClient) {

    var logger = LoggerFactory.getLogger(PkmCardTCGService::class.java)

    fun createCard(apiId: String) : PkmCardDTODjango {
        logger.info(" Criando card {}", apiId)

        var json = client.getCard(apiId)
        json = json?.replace("{\"data\":", "")
        json = json?.dropLast(1)

        val kotlinModule = KotlinModule.Builder()
            .configure(KotlinFeature.NullToEmptyCollection, true)
            .build()
        val mapper = ObjectMapper().registerModule(kotlinModule)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        val tcgDto =  mapper.readValue(json, PkmCardTCGDto::class.java)
        return mapperToPkmDjangoObject(tcgDto)
    }

    fun mapperToPkmDjangoObject(cardApi: PkmCardTCGDto) : PkmCardDTODjango {
        var weaknesses = ""
        if(cardApi.weaknesses != null){
            cardApi.weaknesses.forEach {
                wk -> weaknesses += wk.type + " - " + wk.value
            }
        }

        var retreat = ""
        if(!cardApi.retreatCost.isNullOrEmpty()){
            cardApi.retreatCost.forEach {
                    rt -> retreat += "$rt - "
            }
            retreat = retreat.substring(0, retreat.length - 2)
        }

        val atkList : MutableList<PkmAttackDjango> = ArrayList()

        cardApi.attacks?.forEach { atk -> atkList.add(PkmAttackDjango(
            atk.name,
            getAttackCost(atk.cost),
            getDamageAtk(atk.damage),
            atk.text
        )) }

        println(weaknesses)
        return PkmCardDTODjango(
            cardApi.id,
            "100",
            cardApi.images.small,
            cardApi.images.large,
            null,
            cardApi.name,
            PkmCardEnergyType.getIdByName(cardApi.types[0]),
            cardApi.hp,
            cardApi.subtypes[0],
            if(cardApi.rules.isNullOrEmpty()) "" else cardApi.rules[0],
            weaknesses,
            retreat,
            cardApi.flavorText,
            if(cardApi.abilities.isNullOrEmpty()) "" else cardApi.abilities[0].name,
            if(cardApi.abilities.isNullOrEmpty()) "" else cardApi.abilities[0].text,
            atkList,
        )
    }

    fun getAttackCost(costs :List<String>?) : String {
        var cost = ""
        if(!costs.isNullOrEmpty()){
            costs.forEach { c -> cost += "$c - " }
            cost = cost.substring(0, cost.length - 2)
        }
        return cost
    }

    fun getDamageAtk(atk:String) : String{
        if(atk.isEmpty())
            return "0";

        return atk.replace("[^0-9]".toRegex(), "")

    }

    fun createCardsByPkmName(pkmName: String, pkmId: Int): HashMap<String, PkmCardDTODjango> {
            logger.info(" Criando cards by pkm name {}", pkmName)

            val json = client.getMultipleCards(pkmName)

            val kotlinModule = KotlinModule.Builder()
                .configure(KotlinFeature.NullToEmptyCollection, true)
                .build()
            val mapper = ObjectMapper().registerModule(kotlinModule)
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            val tree = mapper.readTree(json)
            val node = tree.path("data")
            val cardArray: Array<PkmCardTCGDto> = mapper.readValue(node.toString(), Array<PkmCardTCGDto>::class.java)

            val map = HashMap<String, PkmCardDTODjango>()
            val gson = Gson()

            cardArray.forEach { card ->
                    val pkmDjango = mapperToPkmDjangoObject(card)
                    pkmDjango.pokemon_id = pkmId
                    try {
                        djangoClient.createCardOnDjango(gson.toJson(pkmDjango))
                        Thread.sleep(1000)
                    } catch (e: Exception){
                        logger.error(e.message)
                    }
                    map[card.id] = pkmDjango
                }

                return map;
    }
}