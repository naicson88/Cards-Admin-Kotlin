package com.cards.admin.client

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class YuGiOhAPIClient {

    private val apiUrl = "https://db.ygoprodeck.com/api/v7"
    private val client = OkHttpClient()

    fun getCardsFromSet(cardSetName: String) : String ? {

        val request  = Request.Builder().url("${apiUrl}/cardinfo.php?cardset=${cardSetName}").build();

       client.newCall(request).execute().use { response ->
           if(!response.isSuccessful) throw IOException("Unexpected code $response")
           return response.body!!.string()
       }

    }
}