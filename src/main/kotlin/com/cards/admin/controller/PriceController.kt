package com.cards.admin.controller

import cardscommons.dto.PriceDTO
import com.cards.admin.data.strategy.MessageBrokerStrategy
import com.cards.admin.enums.RabbitMQueues
import com.cards.admin.service.PriceService
import com.cards.admin.service.RabbitMQService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/admin/price")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class PriceController(
        val messageBroker: MessageBrokerStrategy,
        val priceService: PriceService
    ) {

    @GetMapping("/update-deck")
    fun updateDeckPrice(@RequestParam deckName: String): ResponseEntity<List<PriceDTO>>{
        val list = priceService.updateDeckPrice(deckName)
        messageBroker.sendMessage(RabbitMQueues.SET_PRICE_QUEUE.toString(), list)

        return ResponseEntity(list, HttpStatus.OK)
    }

    @GetMapping("/update-card")
    fun updateCardPrice(@RequestParam cardName: String) : ResponseEntity<List<PriceDTO>> {
        val prices = priceService.updateCardPrice(cardName)
        messageBroker.sendMessage(RabbitMQueues.CARD_PRICE_QUEUE.toString(), prices)

        return ResponseEntity(prices, HttpStatus.OK)
    }

}