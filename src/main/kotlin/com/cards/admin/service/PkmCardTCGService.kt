package com.cards.admin.service

import com.cards.admin.client.PkmCardTCGApi
import com.cards.admin.dto.PkmAttackDjango
import com.cards.admin.dto.PkmCardDTODjango
import com.cards.admin.dto.PkmCardTCGDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.internal.wait
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class PkmCardTCGService(val client: PkmCardTCGApi) {

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
        if(cardApi.retreatCost != null){
            cardApi.retreatCost.forEach {
                    rt -> retreat += "$rt - "
            }
            retreat = retreat.substring(0, retreat.length - 2)
        }

        val atkList : MutableList<PkmAttackDjango> = ArrayList()

        cardApi.attacks?.forEach { atk -> atkList.add(PkmAttackDjango(
            atk.name,
            getAttackCost(atk.cost),
            atk.damage,
            atk.text
        )) }

        println(weaknesses)
        return PkmCardDTODjango(
            cardApi.id,
            cardApi.name,
            cardApi.hp,
            cardApi.subtypes[0],
            weaknesses,
            retreat,
            atkList,
            cardApi.flavorText,
            cardApi.images.small,
            cardApi.images.large,
            "",
            "",
            "",
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
}