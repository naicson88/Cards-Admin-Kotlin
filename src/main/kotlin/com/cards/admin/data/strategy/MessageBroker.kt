package com.cards.admin.data.strategy

import org.springframework.stereotype.Component


interface MessageBroker {
    fun sendMessage( destiny :String,  message: String)
    fun consume() : String
}