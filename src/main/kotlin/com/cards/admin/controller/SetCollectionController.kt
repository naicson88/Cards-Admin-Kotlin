package com.cards.admin.controller

import cardscommons.dto.AssociationDTO
import cardscommons.dto.SetCollectionDTO
import com.cards.admin.enums.RabbitMQueues
import com.cards.admin.service.RabbitMQService
import com.cards.admin.service.SetCollectionService
import jakarta.validation.Valid
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/admin/set-collection")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class SetCollectionController(val rabbitMQService: RabbitMQService, val service: SetCollectionService) {

    var logger = LoggerFactory.getLogger(SetCollectionController::class.java)

    @PostMapping("/new-collection")
    fun newSetCollection(@Valid @RequestBody dto: SetCollectionDTO, @RequestHeader("Authorization") token: String) :
            ResponseEntity<SetCollectionDTO> {
        logger.info("Starting creating new SetCollection...")
        rabbitMQService.sendMessageAsJson(RabbitMQueues.SET_COLLECTION_QUEUE.toString(), dto)
        logger.info("Message sent successfully to SET_COLLECTION Queue")

        return ResponseEntity(dto, HttpStatus.OK)
    }

    @PostMapping("/new-association")
    fun newAssociation(@Valid @RequestBody dto: AssociationDTO, token: String): ResponseEntity<String>{
        logger.info("Starting creating new Association...")
        service.newAssociation(dto, token)
        logger.info("Association sent successfully!")

        return ResponseEntity(JSONObject.quote("Association sent successfully!"), HttpStatus.OK)

    }
}