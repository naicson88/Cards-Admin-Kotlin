package com.cards.admin.service

import cardscommons.dto.CardYuGiOhAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CardService(
        private val apiCardsService : YuGiOhAPICardsService
) {

    fun getCardsToBeRegistered(cardsNotRegistered : List<Long> ) : List<CardYuGiOhAPI> {
        require(cardsNotRegistered.isNotEmpty()) {"Invalid Cards Not Registered"}
        val cardToBeRegistered : List<CardYuGiOhAPI> = cardsNotRegistered.stream().map { apiCardsService.getCardOnYuGiOhAPI(it) }.toList()
        println(cardToBeRegistered)
        return cardToBeRegistered
    }
}