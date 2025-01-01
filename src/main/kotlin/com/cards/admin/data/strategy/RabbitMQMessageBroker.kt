package com.cards.admin.data.strategy

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component


class RabbitMQMessageBroker(val template: RabbitTemplate) : MessageBroker {
    override fun sendMessage(destiny: String, message: String) {
        try {
            val mapper = ObjectMapper()
            val json  = mapper.writeValueAsString(message)
            template.convertAndSend(destiny, json)
        } catch (e : JsonProcessingException){
            e.printStackTrace()
            throw  e;
        } catch (e: Exception) {
            e.printStackTrace()
            throw  e;
        }
    }

    override fun consume(): String {
        TODO("Not yet implemented")
    }

}