package com.cards.admin.dto


class PkmCardDTODjango(

    val api_id: String?,
    val number: String?,
    val image_small: String,
    val image_large: String,
    var pokemon_id: Int?,
    val card_name: String?,
    val pokemon_type: Int?,
    val hp: String?,
    val stage: String?,
    val ex_rule: String?,
    val weaknesses: String?,
//    val resistance: List<String>?,
    val retreat: String,
    val pokemon_description: String?,
    val ability_name:  String?,
    val ability_description:  String?,
    val attack: List<PkmAttackDjango>,
) {
}

class PkmAttackDjango(
    val name: String?,
    val cost: String?,
    val hit_point: String?,
    val description: String?,
    ){}