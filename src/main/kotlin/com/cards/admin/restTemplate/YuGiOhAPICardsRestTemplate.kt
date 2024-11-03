package com.cards.admin.restTemplate

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Service
class YuGiOhAPICardsRestTemplate {

    val SCHEM : String = "https"
    val HOST : String = "db.ygoprodeck.com";

    fun getCardFromYuGiOhAPI(cardNumber : Long) : String? {
        val restTemplate = RestTemplate()

        val uri : UriComponents = UriComponentsBuilder.newInstance()
                .scheme(SCHEM)
                .host(HOST)
                .path("api/v7/cardinfo.php")
                .queryParam("id", cardNumber)
                .build();

        return restTemplate.getForObject(uri.toString(), String::class.java)
    }
}