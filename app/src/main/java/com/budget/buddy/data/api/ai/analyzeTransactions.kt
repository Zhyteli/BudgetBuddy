package com.budget.buddy.data.api.ai

import com.budget.buddy.domain.cash.CashTransaction
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

suspend fun analyzeTransactions(transactions: List<CashTransaction>): String {
    val prompt = createPrompt(transactions)
    val messages = listOf(
        Message(role = "system", content = "You are a financial assistant."),
        Message(role = "user", content = prompt)
    )
    val requestBody = ChatGPTRequestBody(
        model = "gpt-4-turbo", // Убедитесь, что используете поддерживаемую модель
        messages = messages,
        max_tokens = 512,
        temperature = 0.7
    )

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()



    return try {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenAIService::class.java)
        val apiKey =
            "Bearer sk-proj-q1hHartUSjvlZEIoQo8ZT3BlbkFJ5vYOJqipaJSgwjPlIAJf" // Замените на ваш реальный API-ключ
        val response = service.analyze(apiKey, requestBody)
        if (response.isSuccessful) {
            val result =
                response.body()?.choices?.firstOrNull()?.message?.content ?: "No recommendation"
            result
        } else {
            throw Exception("Failed to get response from OpenAI: ${response.errorBody()?.string()}")
        }
    } catch (e: Exception) {
        e.message.toString()
    }

}

fun createPrompt(transactions: List<CashTransaction>): String {
    val transactionDetails = transactions.joinToString("\n") { transaction ->
        "Amount: ${transaction.amount}, Date: ${transaction.date}, Description: ${transaction.description}, Type: ${transaction.type}"
    }
    return """
        Here are the user's transactions for the month:
        $transactionDetails
        
        Based on this data, please provide financial recommendations to help the user manage their finances better.
    """.trimIndent()
}
