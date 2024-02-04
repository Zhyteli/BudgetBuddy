package com.budget.buddy.data.database

import androidx.room.*
import com.budget.buddy.domain.cash.CashTransaction

@Dao
interface CashTransactionDao {

    @Insert
    suspend fun insert(transaction: CashTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransactions(transactions: List<CashTransaction>)

    @Update
    suspend fun updateTransaction(transaction: CashTransaction)

    @Delete
    suspend fun deleteTransaction(transaction: CashTransaction)

    @Query("DELETE FROM CashTransaction WHERE id IN (:ids)")
    suspend fun deleteTransactionsByIds(ids: List<Int>)

    @Query("SELECT * FROM CashTransaction")
    suspend fun getAllTransactions(): List<CashTransaction>?

    @Query("SELECT * FROM CashTransaction WHERE id = :id")
    suspend fun getTransactionById(id: Int): CashTransaction?

}
