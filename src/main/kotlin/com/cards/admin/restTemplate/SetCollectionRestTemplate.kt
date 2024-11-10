package com.cards.admin.restTemplate

import cardscommons.dto.AssociationDTO
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class SetCollectionRestTemplate {
    val SCHEM = "http"
    val HOST = "localhost:8080"

    fun sendNewAssociation(dto: AssociationDTO, token: String): ResponseEntity<String>{
        val restTemplate = RestTemplate()
        val header = HttpHeaders()
        header.set("Authorization", token)
        header.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity(dto, header)

        val uri = UriComponentsBuilder.newInstance().scheme(SCHEM).host(HOST)
                .path("yugiohAPI/collection/new-association")
                .build()

        return restTemplate.postForEntity(uri.toString(), entity, String::class.java)
    }
}