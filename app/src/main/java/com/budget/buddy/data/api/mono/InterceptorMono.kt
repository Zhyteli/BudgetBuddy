package com.budget.buddy.data.api.mono

import okhttp3.Interceptor
import okhttp3.Response

class InterceptorMono : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Token", "uNh6UFqepskDDOevuYAU_VEH45nmGtQExTHuccB0A0wE")
            .build()
        return chain.proceed(request)
    }
}