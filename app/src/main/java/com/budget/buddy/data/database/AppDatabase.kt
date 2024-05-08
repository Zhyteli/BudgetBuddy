package com.budget.buddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.domain.user.Time

@Database(
    entities = [
        CashTransaction::class,
        MainUserDataMouth::class,
        Time::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun mainUserDataMouthDao(): MainUserDataMouthDao
    abstract fun timeDao(): TimeDao
}