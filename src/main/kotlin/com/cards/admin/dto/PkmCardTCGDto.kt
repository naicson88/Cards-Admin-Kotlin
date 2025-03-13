package com.cards.admin.dto

class PkmCardTCGDto (
    val id: String,
    val name: String?,
    val hp: String,
    val types: List<String>,
    val attacks: List<PkmAttack>?,
    val weaknesses: List<PkmWeaknesses>?,
    val retreatCost: List<String>?,
    val flavorText: String?,
    val images: PkmImages,
    val subtypes: List<String>,
    val abilities: List<PkmAbilities>?,
    val rules : List<String>?
) {
}

class PkmAttack(
    val name: String?,
    val cost: List<String>?,
    val damage: String,
    val text: String?,

){}

class PkmWeaknesses(
    val type: String?,
    val value: String?
    ){}

class PkmImages(
    val small: String,
    val large: String
){}

class PkmAbilities (
    val name: String,
    val text: String
){}