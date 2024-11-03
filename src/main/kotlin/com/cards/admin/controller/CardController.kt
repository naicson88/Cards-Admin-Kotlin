package com.cards.admin.controller

import com.cards.admin.restTemplate.CardRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import cardscommons.dto.CardYuGiOhAPI
import com.cards.admin.service.CardService


@RestController
@RequestMapping("v1/admin/card")
class CardController(
        private val restTemplate: CardRestTemplate,
        private val cardService: CardService
) {

    @PostMapping("/search-cards")
    fun searchCards(@RequestBody cardsNumbers: List<Long>, @RequestHeader("Authorization") token: String): ResponseEntity<LongArray> {
        return restTemplate.findCardsNotRegistered(cardsNumbers, token)
    }


    @PostMapping("/cards-not-registered")
    fun getCardsFromYuGiOhAPINotRegistered(@RequestBody cardsNumbers: List<Long> ) : ResponseEntity<List<CardYuGiOhAPI>> {
        val list : List<CardYuGiOhAPI> = this.cardService.getCardsToBeRegistered(cardsNumbers)
        return ResponseEntity(list, HttpStatus.OK)
    }
}