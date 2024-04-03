package com.budget.buddy.domain.mapper

import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.mono.UsersBankDetails

object MapperCashCardToUsersBank {

    fun cashCardToUsersBank(cashTransaction: CashTransaction):UsersBankDetails{
        return UsersBankDetails(
            id = cashTransaction.id.toString(),
            time = cashTransaction.date,
            description = cashTransaction.description ?: "",
            amount = cashTransaction.amount,
            operationAmount = cashTransaction.amount.toInt(),
            currencyCode = 0,
            commissionRate = 0,
            cashbackAmount = 0,
            balance = 0,
            comment = "",
            receiptId = "",
            counterEdrpou = "",
            counterIban = "",
            counterName = "",
        )
    }

    fun usersBankToCashCard(usersBankDetails: UsersBankDetails):CashTransaction{
        return CashTransaction(
            id = usersBankDetails.id.toInt(),
            amount = usersBankDetails.amount,
            date = usersBankDetails.time,
            description = usersBankDetails.description,
            type = usersBankDetails.mcc,
        )
    }
}