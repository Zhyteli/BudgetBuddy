package com.budget.buddy.domain.cash.usecase

import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class UpdateTransactionUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(transaction: CashTransaction) =
        repository.updateTransaction(transaction)
}