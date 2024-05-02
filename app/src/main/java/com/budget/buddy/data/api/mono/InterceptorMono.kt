package com.budget.buddy.data.api.mono

import okhttp3.Interceptor
import okhttp3.Response

class InterceptorMono : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Token", "u8PfX2hoxKcqh-JUWvoQ-PgEp_W1WTdbwJm7eIdecGdY")
            .build()
        return chain.proceed(request)
    }
}