package com.cards.admin.restTemplate

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Service
 class CardRestTemplate {
    private val SCHEM = "http"
    private val HOST = "localhost:8080"
    private val restTemplate = RestTemplate()

    fun findCardsNotRegistered(cardsNumbers : List<Long>, token: String): ResponseEntity<LongArray> {

        val header = createHeader(token)
        val entity : HttpEntity<List<Long>> = HttpEntity(cardsNumbers, header);

        val uri: UriComponents = UriComponentsBuilder.newInstance()
                .scheme(SCHEM)
                .host(HOST)
                .path("yugiohAPI/cards/search-cards-not-registered")
                .queryParam("cardNumbers", cardsNumbers)
                .build()

        return restTemplate.postForEntity(uri.toString(), entity, LongArray::class.java)
    }

    private fun createHeader(token : String) : HttpHeaders {
        val header  = HttpHeaders();
        header.add("Authorization", token);
        header.contentType = MediaType.APPLICATION_JSON
        return header
    }
}