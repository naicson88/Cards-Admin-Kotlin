package com.cards.admin.utils

import com.cards.admin.data.strategy.KafkaMessageBroker
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class Utils {

    private var logger: Logger = LoggerFactory.getLogger(Utils::class.java)
    fun convertObjectIntoJson(obj: Any) : String {
        try {
            val mapper = ObjectMapper()
            return mapper.writeValueAsString(obj)
        } catch (e : JsonProcessingException){
            logger.error(" #Utils -> Error when trying convet Object into Json")
            e.printStackTrace()
            throw  e;
        } catch (e: Exception) {
            e.printStackTrace()
            throw  e;
        }
    }
}