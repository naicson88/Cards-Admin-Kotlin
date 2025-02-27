package com.cards.admin.controller

import cardscommons.dto.CollectionDeckDTO
import cardscommons.dto.KonamiDeckDTO
import com.cards.admin.data.strategy.MessageBrokerStrategy
import com.cards.admin.enums.RabbitMQueues
import com.cards.admin.service.DeckService
import com.cards.admin.service.RabbitMQService
import com.cards.admin.utils.Utils
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
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
class DeckController(val deckService: DeckService, val messageBroker: MessageBrokerStrategy) {

    @PostMapping("/new")
    fun createDeck(
            @Valid @RequestBody konamiDeck: KonamiDeckDTO,
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<KonamiDeckDTO> {
        val kDeck = deckService.createNewKonamiDeckWithCards(konamiDeck, token)

        messageBroker.sendMessage(RabbitMQueues.DECK_QUEUE.toString(), kDeck)

        return ResponseEntity(kDeck, HttpStatus.CREATED)
    }
}