package com.cards.admin.enums

enum class PkmCardEnergyType(val id: Int) {
    FIRE(3),
    COLORLESS(4),
    DARK(5),
    ELECTRIC(6),
    FIGHTING(7),
    GRASS(8),
    PSYCHIC(9),
    STEEL(10),
    WATER(11);

    companion object {
        fun getIdByName(name: String): Int? {
            return entries.find { it.name == name.uppercase() }?.id
        }
    }
}