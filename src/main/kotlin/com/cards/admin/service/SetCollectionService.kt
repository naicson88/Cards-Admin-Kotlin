package com.cards.admin.service

import cardscommons.dto.AssociationDTO
import com.cards.admin.restTemplate.SetCollectionRestTemplate
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SetCollectionService(val deckService: DeckService, val restTemplate: SetCollectionRestTemplate) {
    var logger: Logger = LoggerFactory.getLogger(SetCollectionService::class.java)

    fun newAssociation(@Valid dto: AssociationDTO, token: String): AssociationDTO {
        val response = restTemplate.sendNewAssociation(dto,token)

        if(response.statusCode != HttpStatus.OK)
            throw RuntimeException("It was not possible register a new Association")

        return dto
    }
}