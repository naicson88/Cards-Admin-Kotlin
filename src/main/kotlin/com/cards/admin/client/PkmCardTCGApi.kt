package com.cards.admin.client

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class PkmCardTCGApi {

    private val apiUrl = "https://api.pokemontcg.io/v2/cards/"
    private val client = OkHttpClient()

    fun getCard(apiId: String) : String ? {
        val request  = Request.Builder().url("${apiUrl}/${apiId}").build();

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }

    }
}