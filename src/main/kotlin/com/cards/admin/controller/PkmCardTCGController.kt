package com.cards.admin.controller

import com.cards.admin.dto.PkmCardDTODjango
import com.cards.admin.dto.PkmCardTCGDto
import com.cards.admin.service.PkmCardTCGService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/admin/pkm")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class PkmCardTCGController(val service: PkmCardTCGService) {

    @PostMapping("/card/{apiId}")
    fun createDeck(@PathVariable apiId: String
    ) : ResponseEntity<PkmCardDTODjango> {
        val card = service.createCard(apiId)
        return ResponseEntity(card, HttpStatus.CREATED)
    }

    @PostMapping("/cards/{pkmName}/{pkmId}")
    fun createCardsByPkmName(@PathVariable pkmName: String, @PathVariable pkmId: Int
    ) : ResponseEntity<HashMap<String, PkmCardDTODjango>> {
        val card = service.createCardsByPkmName(pkmName, pkmId)
        return ResponseEntity(card, HttpStatus.CREATED)
    }
}