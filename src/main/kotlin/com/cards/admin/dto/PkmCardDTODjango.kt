package com.cards.admin.dto

class PkmCardDTODjango(

    val api_id: String?,
    val card_name: String?,
    val hp: String?,
    val stage: String?,
    val weaknesses: String?,
//    val resistance: List<String>?,
    val retreat: String,
    val attack: List<PkmAttackDjango>,
    val pokemon_description: String?,
    val image_small: String,
    val image_large: String,
    val ex_rule: String?,
    val ability_name:  String?,
    val ability_description:  String?,

) {
}

class PkmAttackDjango(
    val name: String?,
    val cost: String?,
    val hit_point: String?,
    val description: String?,
    ){}