package com.budget.buddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.budget.buddy.domain.ai.AiAnalysis
import com.budget.buddy.domain.cash.CashTransaction
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.domain.user.Time

@Database(
    entities = [
        CashTransaction::class,
        MainUserDataMouth::class,
        AiAnalysis::class,
        Time::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun mainUserDataMouthDao(): MainUserDataMouthDao
    abstract fun timeDao(): TimeDao
    abstract fun aiAnalysisDao(): AiAnalysisDao
}