package com.cards.admin.service

import cardscommons.dto.CardYuGiOhAPI
import cardscommons.dto.RelDeckCardsDTO
import cardscommons.exceptions.ErrorMessage
import com.cards.admin.dto.CardDTO
import com.cards.admin.restTemplate.CardRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class CardService(
         val apiCardsService : YuGiOhAPICardsService,
         val cardRestTemplate: CardRestTemplate

) {

    fun getCardsToBeRegistered(cardsNotRegistered : List<Long> ) : List<CardYuGiOhAPI> {
        require(cardsNotRegistered.isNotEmpty()) {"Invalid Cards Not Registered"}
        return cardsNotRegistered.stream().map { apiCardsService.getCardOnYuGiOhAPI(it) }.toList()
    }

    fun addNewCardToDeck(card: CardDTO, token: String): CardDTO {
        val rel = RelDeckCardsDTO()
        rel.cardNumber = card.number

        val cardsNotRegistered : LongArray ? = verifyCardsNotRegistered(listOf(rel), token)
        val listCardsNotRegistered = cardsNotRegistered?.asList()

        if(!listCardsNotRegistered.isNullOrEmpty())
            card.cardsToBeRegistered = getCardsToBeRegistered(listCardsNotRegistered)

        return card
    }

    fun verifyCardsNotRegistered(listRelDeckCards: List<RelDeckCardsDTO>, token: String) : LongArray? {
        validVerifyCardsNotRegistered(listRelDeckCards, token)

        val cardNumberOfADeck = getCardNumberFromListRelDeckCards(listRelDeckCards)

        val cardsNotRegistered : ResponseEntity<LongArray> ? = cardRestTemplate.findCardsNotRegistered(cardNumberOfADeck, token)

        if(cardsNotRegistered == null || !cardsNotRegistered.statusCode.is2xxSuccessful)
            throw ErrorMessage("It was not possible verify Card Not Registered");

        return cardsNotRegistered.body
    }

    fun getCardNumberFromListRelDeckCards(listRelDeckCards: List<RelDeckCardsDTO>) : List<Long> {

        val cardNumbersList : List<Long> = listRelDeckCards.stream()
                .filter { it.cardNumber != null }
                .distinct()
                .map { it.cardNumber }
                .toList()

        require(cardNumbersList.isNotEmpty()) {"It was not possible get Card Numbers"}

        return cardNumbersList
    }

    private fun validVerifyCardsNotRegistered(listRelDeckCards: List<RelDeckCardsDTO>?, token: String?) {
        require(!listRelDeckCards.isNullOrEmpty()) { "Invalid Konami Deck informed." }
        require(!token.isNullOrBlank()) { "Invalid Token" }
    }
}