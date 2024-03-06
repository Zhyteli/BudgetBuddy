package com.budget.buddy.domain.mono

import com.google.gson.annotations.SerializedName

data class UsersBankDetails(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("time")
    val time: Long = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("mcc")
    val mcc: Int = 0,
    @SerializedName("amount")
    val amount: Int = 0,
    @SerializedName("operationAmount")
    val operationAmount: Int = 0,
    @SerializedName("currencyCode")
    val currencyCode: Int = 0,
    @SerializedName("commissionRate")
    val commissionRate: Int = 0,
    @SerializedName("cashbackAmount")
    val cashbackAmount: Int = 0,
    @SerializedName("balance")
    val balance: Int = 0,
    @SerializedName("comment")
    val comment: String = "",
    @SerializedName("receiptId")
    val receiptId: String = "",
    @SerializedName("counterEdrpou")
    val counterEdrpou: String = "",
    @SerializedName("counterIban")
    val counterIban: String = "",
    @SerializedName("counterName")
    val counterName: String = "",
)
