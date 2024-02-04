package com.budget.buddy.domain.cash.usecase

import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class DeleteTransactionUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(transaction: CashTransaction) =
        repository.deleteTransaction(transaction)
}