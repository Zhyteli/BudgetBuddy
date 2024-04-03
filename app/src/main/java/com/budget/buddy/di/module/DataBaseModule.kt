package com.budget.buddy.di.module

import android.content.Context
import androidx.room.Room
import com.budget.buddy.data.database.AppDatabase
import com.budget.buddy.data.database.CashTransactionDao
import com.budget.buddy.data.database.MainUserDataMouthDao
import com.budget.buddy.data.database.TimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext application: Context): Context = application

    @Provides
    @Singleton
    fun provideDatabase(applicationContext: Context): AppDatabase =
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "cash_transaction.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCashTransactionDao(database: AppDatabase): CashTransactionDao =
        database.cashTransactionDao()

    @Provides
    fun provideMainUserDataMouthDao(database: AppDatabase): MainUserDataMouthDao =
        database.mainUserDataMouthDao()

    @Provides
    fun provideTimeDao(database: AppDatabase): TimeDao =
        database.timeDao()

}