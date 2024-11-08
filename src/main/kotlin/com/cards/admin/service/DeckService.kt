package com.cards.admin.service

import cardscommons.dto.CardYuGiOhAPI
import cardscommons.dto.KonamiDeckDTO
import cardscommons.dto.RelDeckCardsDTO
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
        logger.info("Creating new Konami Deck {}", konamiDeck.nome)

        val listRelDeckCards = apiService.getCardsOfADeck(konamiDeck.requestSource)
        //It necessary to check if all cards are already registered in cards' table
        konamiDeck.relDeckCards = listRelDeckCards
        konamiDeck.relDeckCards.forEach { it.isSpeedDuel = konamiDeck.isSpeedDuel }
        //konamiDeck.cardsToBeRegistered = checkCardsNotRegistered(listRelDeckCards, token)

        return konamiDeck
    }

    fun checkCardsNotRegistered(listRelDeckCards: List<RelDeckCardsDTO>, token: String) : List<CardYuGiOhAPI> ? {
        val cardsNotRegistered: LongArray ? = cardService.verifyCardsNotRegistered(listRelDeckCards, token)  //71

        val distinctCards =  cardsNotRegistered?.asList()?.distinct()?.toList() ?: return null

        return cardService.getCardsToBeRegistered(distinctCards)

    }
}