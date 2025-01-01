package com.cards.admin.data.strategy

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component


@Component
class KafkaMessageBroker(val template: KafkaTemplate<String, String>) : MessageBroker {

    var logger: Logger = LoggerFactory.getLogger(KafkaMessageBroker::class.java)
    override fun sendMessage(destiny: String, message: String) {
        logger.info(" -> Sending message to Kafka topic: {}", destiny)
        template.send(destiny, message)
    }

    override fun consume(): String {
        TODO("Not yet implemented")
    }

}