package com.budget.buddy.data.impl

import com.budget.buddy.data.database.CashTransactionDao
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository
import com.budget.buddy.domain.cash.CashTransaction
import javax.inject.Inject

class CashTransactionRepositoryImpl @Inject constructor(
    private val cashTransactionDao: CashTransactionDao,
) : CashTransactionsRepository {
    override suspend fun addCashTransaction(transaction: CashTransaction) {
        cashTransactionDao.insert(transaction)
    }

    override suspend fun updateTransactions(transactions: List<CashTransaction>) {
        cashTransactionDao.updateTransactions(transactions)
    }

    override suspend fun updateTransaction(transaction: CashTransaction) {
        cashTransactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: CashTransaction) {
        cashTransactionDao.deleteTransaction(transaction)
    }

    override suspend fun deleteTransactionsByIds(ids: List<Int>) {
        cashTransactionDao.deleteTransactionsByIds(ids)
    }

    override suspend fun getAllTransactions(): List<CashTransaction>? {
        return cashTransactionDao.getAllTransactions()
    }

    override suspend fun getTransactionById(id: Int): CashTransaction? {
        return cashTransactionDao.getTransactionById(id)
    }
}