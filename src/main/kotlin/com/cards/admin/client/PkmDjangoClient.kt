package com.cards.admin.client

import com.cards.admin.dto.PkmCardDTODjango
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class PkmDjangoClient {

    private val apiUrl = "http://localhost:8000/card/create"
    private val client = OkHttpClient()

    fun createCardOnDjango(dto: String) : String  {

        val request  = Request.Builder()
            .url(apiUrl)
            .post(dto.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .build();

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }
    }

    companion object {
        val MEDIA_TYPE_MARKDOWN = "application/json; charset=utf-8".toMediaType()
    }
}
