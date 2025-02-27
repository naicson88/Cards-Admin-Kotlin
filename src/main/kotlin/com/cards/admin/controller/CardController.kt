package com.cards.admin.controller

import cardscommons.dto.CardYuGiOhAPI
import com.cards.admin.data.strategy.MessageBrokerStrategy
import com.cards.admin.dto.CardDTO
import com.cards.admin.enums.RabbitMQueues
import com.cards.admin.restTemplate.CardRestTemplate
import com.cards.admin.service.CardService
import com.cards.admin.service.RabbitMQService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("v1/admin/card")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class CardController(
         val restTemplate: CardRestTemplate,
         val cardService: CardService,
         val messageBroker: MessageBrokerStrategy
) {

    @PostMapping("/search-cards")
    fun searchCards(@RequestBody cardsNumbers: List<Long>, @RequestHeader("Authorization") token: String): ResponseEntity<LongArray> ? {
        return restTemplate.findCardsNotRegistered(cardsNumbers, token)
    }


    @PostMapping("/cards-not-registered")
    fun getCardsFromYuGiOhAPINotRegistered(@RequestBody cardsNumbers: List<Long> ) : ResponseEntity<List<CardYuGiOhAPI>> {
        val list : List<CardYuGiOhAPI> = this.cardService.getCardsToBeRegistered(cardsNumbers)
        return ResponseEntity(list, HttpStatus.OK)
    }

    @PostMapping("/add-new-card-to-deck")
    fun addNewCardToDeck(@Valid @RequestBody cardDto : CardDTO, token: String) : ResponseEntity<CardDTO> {
        val cardAdded = cardService.addNewCardToDeck(cardDto, token)

        messageBroker.sendMessage(RabbitMQueues.CARD_QUEUE.toString(), cardAdded)

        return ResponseEntity(cardAdded, HttpStatus.CREATED)
    }
}