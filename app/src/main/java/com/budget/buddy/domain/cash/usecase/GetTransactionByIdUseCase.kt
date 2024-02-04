package com.budget.buddy.domain.cash.usecase

import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class GetTransactionByIdUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(id: Int) = repository.getTransactionById(id)
}