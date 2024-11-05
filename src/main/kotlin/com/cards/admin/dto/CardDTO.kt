package com.cards.admin.dto

import cardscommons.dto.CardYuGiOhAPI
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class CardDTO(
        @field:NotBlank(message = "Card Name cannot be empty")
        val name: String,

        @field:NotNull(message = "Card Number cannot be empty")
        val number: Long,

        @field:NotNull(message = "Deck ID cannot be empty")
        val deckId: Long,

        @field:NotBlank(message = "Card Rarity cannot be empty")
        val rarity: String,

        @field:NotBlank(message = "Card Rarity Code cannot be empty")
        val rarityCode: String,

        @field:NotBlank(message = "Card Rarity Details cannot be empty")
        val rarityDetails: String,

        @field:NotBlank(message = "Card SetCode cannot be empty")
        val cardSetCode: String,

        @field:NotNull(message = "isSpeedDuel cannot be empty")
        val isSpeedDuel: Boolean,

        @field:NotNull(message = "Price cannot be empty")
        var price: Double = 0.0,

        var cardsToBeRegistered: List<CardYuGiOhAPI>
) {
}