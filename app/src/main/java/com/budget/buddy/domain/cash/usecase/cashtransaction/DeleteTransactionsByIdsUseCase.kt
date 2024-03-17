package com.budget.buddy.domain.cash.usecase.cashtransaction

import com.budget.buddy.domain.cash.repository.CashTransactionsRepository

class DeleteTransactionsByIdsUseCase(private val repository: CashTransactionsRepository) {
    suspend operator fun invoke(ids: List<Int>) =
        repository.deleteTransactionsByIds(ids)
}