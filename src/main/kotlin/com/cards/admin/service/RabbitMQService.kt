package com.cards.admin.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitMQService(
        val template: RabbitTemplate
) {
    fun sendMessageAsJson(queueName: String,message: Any ): Unit {
        try {
            val mapper = ObjectMapper()
            val json  = mapper.writeValueAsString(message)
            template.convertAndSend(queueName, json)

        } catch (e : JsonProcessingException){
            e.printStackTrace()
            throw  e;
        } catch (e: Exception) {
            e.printStackTrace()
            throw  e;
        }
    }
}