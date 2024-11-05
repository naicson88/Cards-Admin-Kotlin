package com.cards.admin.service

import cardscommons.dto.KonamiDeckDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeckService(
        val apiService: YuGiOhAPICardsService,
        val cardService: CardService
) {
    var logger: Logger = LoggerFactory.getLogger(DeckService::class.java)

    fun createNewKonamiDeckWithCards(konamiDeck: KonamiDeckDTO, token: String): KonamiDeckDTO {

        val listRelDeckCards = apiService.getCardsOfADeck(konamiDeck.requestSource)
        println(listRelDeckCards)

        return konamiDeck
    }
}