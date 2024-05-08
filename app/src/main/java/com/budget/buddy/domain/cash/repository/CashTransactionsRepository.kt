package com.budget.buddy.domain.cash.repository

import com.budget.buddy.domain.cash.CashTransaction

interface CashTransactionsRepository {
    suspend fun addCashTransaction(transaction: CashTransaction)
    suspend fun updateTransactions(transactions: List<CashTransaction>)
    suspend fun updateTransaction(transaction: CashTransaction)
    suspend fun deleteTransaction(transaction: CashTransaction)
    suspend fun deleteTransactionsByIds(ids: List<Int>)
    suspend fun getAllTransactions(): List<CashTransaction>?
    suspend fun getTransactionById(id: String): CashTransaction?
}