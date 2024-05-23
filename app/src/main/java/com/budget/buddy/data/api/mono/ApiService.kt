package com.budget.buddy.data.api.mono

import com.budget.buddy.domain.mono.UsersBankDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("personal/statement/{account}/{from}/{to}")
    suspend fun getUserData(
        @Path("account") defalt: String = "0",
        @Path("from") from: String,
        @Path("to") to: String,
        @Header("X-Token") token: String
    ): Response<Array<UsersBankDetails>>
//
//    @GET("personal/client-info")
//    suspend fun getClientInfo(): Response<Accounts>

}