package com.budget.buddy.domain.cash.usecase.cashtransaction

import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class UpdateTransactionsUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(transactions: List<CashTransaction>) =
        repository.updateTransactions(transactions)
}