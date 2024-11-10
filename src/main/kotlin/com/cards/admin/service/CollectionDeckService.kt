package com.cards.admin.service

import cardscommons.dto.CollectionDeckDTO
import cardscommons.dto.RelDeckCardsDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
class CollectionDeckService(
        val apiService: YuGiOhAPICardsService,
        val deckService: DeckService,
        ) {

    fun createNewCollectionDeck(collDeck: CollectionDeckDTO, token: String): CollectionDeckDTO {
        require(collDeck.setId != null) { "Invalid Set ID to register Collection Deck" }

        collDeck.relDeckCards = getListRelDeckCardsForNewCollectionDeck(collDeck)
        //It necessary to check if all cards are already registered in cards' table
        collDeck.cardsToBeRegistered = deckService.checkCardsNotRegistered(collDeck.relDeckCards, token)
        return collDeck;
    }

    fun getListRelDeckCardsForNewCollectionDeck(collDeck: CollectionDeckDTO): List<RelDeckCardsDTO> {
        val isFiltered = collDeck.filterSetCode.isNullOrBlank()
        return if (!isFiltered) getFilteredCards(collDeck) else apiService.getCardsOfADeck(collDeck.requestSource)

    }

    fun getFilteredCards(collDeck: CollectionDeckDTO): List<RelDeckCardsDTO> {
        val filtersSetCode = collDeck.filterSetCode.split(";")
        val listRelDeckCards = mutableListOf<RelDeckCardsDTO>()

        apiService.getCardsOfADeck(collDeck.requestSource)
                .forEach {
                    for (setCode in filtersSetCode) {
                        if (it.cardSetCode.contains(setCode))
                            listRelDeckCards.add(it);
                        break
                    }
                }

        return listRelDeckCards
    }

    fun createNewDeckCollectionYugipedia(collDeck: CollectionDeckDTO, token: String): CollectionDeckDTO {
        collDeck.cardsToBeRegistered = deckService.checkCardsNotRegistered(collDeck.relDeckCards, token)
        collDeck.relDeckCards.forEach { it.dt_criacao = Date() }
        return collDeck
    }
}