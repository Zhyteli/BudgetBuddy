package com.budget.buddy.data.api.ai

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun analyze(
        @Header("Authorization") token: String,
        @Body requestBody: ChatGPTRequestBody
    ): Response<GPTResponse>
}

data class ChatGPTRequestBody(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int,
    val temperature: Double
)

data class Message(
    val role: String,
    val content: String
)

data class GPTResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)