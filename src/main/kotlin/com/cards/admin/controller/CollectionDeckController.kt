package com.cards.admin.controller

import cardscommons.dto.CollectionDeckDTO
import com.cards.admin.data.strategy.MessageBrokerStrategy
import com.cards.admin.enums.RabbitMQueues
import com.cards.admin.service.CollectionDeckService
import com.cards.admin.service.RabbitMQService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/admin/collection-deck")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class CollectionDeckController(
        val collService: CollectionDeckService,
        val messageBroker: MessageBrokerStrategy
) {


    @PostMapping("/new-deck-collection-yugipedia")
    fun registerCollection(
            @RequestBody collDeck: CollectionDeckDTO,
            @RequestHeader("Authorization") token: String
    ) : ResponseEntity<CollectionDeckDTO> {

        val collDeckCreated = collService.createNewCollectionDeck(collDeck, token)

        messageBroker.sendMessage(RabbitMQueues.DECK_COLLECTION_QUEUE.toString(), collDeckCreated)

        return ResponseEntity(collDeckCreated, HttpStatus.CREATED)
    }

    @PostMapping("/new-deck-collection")
    fun createNewDeckCollectionYugipedia(
        @RequestBody collDeck: CollectionDeckDTO,
        @RequestHeader("Authorization") token: String
    ) : ResponseEntity<String> {
        val newYugipediaDeck = collService.createNewDeckCollectionYugipedia(collDeck, token)
        messageBroker.sendMessage(RabbitMQueues.DECK_COLLECTION_QUEUE.toString(), newYugipediaDeck)
        return ResponseEntity("Deck Collection Yugipedia created", HttpStatus.CREATED)
    }
}