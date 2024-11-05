package com.cards.admin.controller

import cardscommons.dto.KonamiDeckDTO
import com.cards.admin.service.DeckService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/admin/deck")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class DeckController(val deckService: DeckService) {

    @PostMapping("/new")
    fun createDeck(
            @Valid @RequestBody konamiDeck: KonamiDeckDTO,
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<KonamiDeckDTO> {
        val kDeck = deckService.createNewKonamiDeckWithCards(konamiDeck, token)

        return ResponseEntity(kDeck, HttpStatus.CREATED)
    }
}