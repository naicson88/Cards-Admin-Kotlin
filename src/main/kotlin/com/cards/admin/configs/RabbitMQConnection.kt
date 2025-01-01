package com.cards.admin.configs

import com.cards.admin.enums.RabbitMQueues
import jakarta.annotation.PostConstruct
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.stereotype.Component

@Component
class RabbitMQConnection(val admin: AmqpAdmin) {

    val NOME_EXCHANGE = "amq.direct"

    private fun queue(queueName: String): Queue {
        return Queue(queueName, true, false, false)
    }
    private fun directExchange(): DirectExchange {
        return DirectExchange(NOME_EXCHANGE)
    }

    @PostConstruct
    fun add(){
        createDeckQueue()
        createSetCollectionQueue()
        createDeckCollectionQueue()
        createCardQueue()
        createSetPriceQueue()
    }
    private fun relationship(queue: Queue, directExchange: DirectExchange):Binding {
        return Binding(queue.name, Binding.DestinationType.QUEUE, directExchange.name, queue.name, null)
    }

    fun declareCreation(queue: Queue, exchange: DirectExchange, binding: Binding) {
        admin.declareQueue(queue)
        admin.declareExchange(exchange)
        admin.declareBinding(binding)

    }

    private fun createDeckQueue() {
        val deckQueue = queue(RabbitMQueues.DECK_QUEUE.toString())
        val change = directExchange()
        val deckBinding = relationship(deckQueue, change)
        declareCreation(deckQueue, change, deckBinding)
    }

    private fun createSetCollectionQueue() {
        val setCollectionQueue = queue(RabbitMQueues.SET_COLLECTION_QUEUE.toString())
        val change = directExchange()
        val collectionBinding = relationship(setCollectionQueue, change)
        declareCreation(setCollectionQueue, change, collectionBinding)
    }

    private fun createDeckCollectionQueue() {
        val deckCollectionQueue = queue(RabbitMQueues.DECK_COLLECTION_QUEUE.toString())
        val change = directExchange()
        val collectionBinding = relationship(deckCollectionQueue, change)
        declareCreation(deckCollectionQueue, change, collectionBinding)
    }

    private fun createCardQueue() {
        val cardQueue = queue(RabbitMQueues.CARD_QUEUE.toString())
        val change = directExchange()
        val cardBinding = relationship(cardQueue, change)
        declareCreation(cardQueue, change, cardBinding)
    }

    private fun createSetPriceQueue() {
        val setPriceQueue = queue(RabbitMQueues.SET_PRICE_QUEUE.toString())
        val change = directExchange()
        val setPriceBinding = relationship(setPriceQueue, change)
        declareCreation(setPriceQueue, change, setPriceBinding)
    }

}