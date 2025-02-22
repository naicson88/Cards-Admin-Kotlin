package com.cards.admin.service

import com.cards.admin.client.PkmCardTCGApi
import com.cards.admin.dto.PkmCardTCGDto
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.internal.Intrinsics.Kotlin


@Service
class PkmCardTCGService(val client: PkmCardTCGApi) {

    var logger = LoggerFactory.getLogger(PkmCardTCGService::class.java)

    fun createCard(apiId: String) : PkmCardTCGDto {
        logger.info(" Criando card {}", apiId)

        var json = client.getCard(apiId)
        json = json?.replace("{\"data\":", "")
        json = json?.dropLast(1)

        val kotlinModule = KotlinModule.Builder()
            .configure(KotlinFeature.NullToEmptyCollection, true)
            .build()
        val mapper = ObjectMapper().registerModule(kotlinModule)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.readValue(json, PkmCardTCGDto::class.java)
    }
}