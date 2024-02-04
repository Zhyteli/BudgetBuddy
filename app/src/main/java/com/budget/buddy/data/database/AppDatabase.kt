package com.budget.buddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.budget.buddy.domain.cash.CashTransaction

@Database(entities = [CashTransaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cashTransactionDao(): CashTransactionDao
}