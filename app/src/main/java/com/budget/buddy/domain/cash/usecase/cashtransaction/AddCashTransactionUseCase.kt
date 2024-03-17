package com.budget.buddy.domain.cash.usecase.cashtransaction

import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class AddCashTransactionUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(transaction: CashTransaction) =
        repository.addCashTransaction(transaction)
}