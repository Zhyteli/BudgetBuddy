package com.budget.buddy.domain.cash.usecase

import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class GetAllTransactionsUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke() = repository.getAllTransactions()
}