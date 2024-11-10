package com.cards.admin.client

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class PriceClient {

    private val apiUrl = "https://yugiohprices.com/api"
    private val client = OkHttpClient()

    fun getDeckPrice(cardSetName: String) : String ? {
        val request  = Request.Builder().url("${apiUrl}/set_data/${cardSetName}").build();

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }

    }

    fun getCardPrice(cardName: String) : String ? {
        val request  = Request.Builder().url("${apiUrl}/get_card_prices/${cardName}").build();

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }

    }
}