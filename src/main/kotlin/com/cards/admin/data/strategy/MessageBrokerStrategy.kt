package com.cards.admin.data.strategy

import com.cards.admin.utils.Utils
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class MessageBrokerStrategy(private var messageBroker: MessageBroker,
                            private val kafkaTemplate : KafkaTemplate<String, String>,
                            private val rabbitMQTemplate : RabbitTemplate,
                            private val utils : Utils
) {

    @Value("\${use-broker}")
    lateinit var broker: String
    fun sendMessage( destiny: String,  obj: Any) {
        val message = utils.convertObjectIntoJson(obj)

        messageBroker = if ("KAFKA" == broker) {
            KafkaMessageBroker(kafkaTemplate)
        } else {
            RabbitMQMessageBroker(rabbitMQTemplate)
        }
        messageBroker.sendMessage(destiny, message)
    }
}