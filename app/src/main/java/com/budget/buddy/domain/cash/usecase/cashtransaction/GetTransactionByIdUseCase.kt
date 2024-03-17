package com.budget.buddy.domain.cash.usecase.cashtransaction

import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class GetTransactionByIdUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(id: Int) = repository.getTransactionById(id)
}