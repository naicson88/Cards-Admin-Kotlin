package com.cards.admin.service

import cardscommons.dto.CardYuGiOhAPI
import com.cards.admin.restTemplate.YuGiOhAPICardsRestTemplate
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class YuGiOhAPICardsService {
    @Autowired
    private val apiCardsRest : YuGiOhAPICardsRestTemplate ? = null

    var logger: Logger = LoggerFactory.getLogger(YuGiOhAPICardsService::class.java)

    fun  getCardOnYuGiOhAPI(cardNumber : Long) : CardYuGiOhAPI {
        val json : String? = apiCardsRest?.getCardFromYuGiOhAPI(cardNumber)
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

}