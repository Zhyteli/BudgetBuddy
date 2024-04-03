package com.budget.buddy.di.module

import com.budget.buddy.data.database.CashTransactionDao
import com.budget.buddy.data.database.TimeDao
import com.budget.buddy.data.impl.CashTransactionRepositoryImpl
import com.budget.buddy.data.impl.WorkTime
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository
import com.budget.buddy.domain.cash.usecase.cashtransaction.AddCashTransactionUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.DeleteTransactionUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.DeleteTransactionsByIdsUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.GetAllTransactionsUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.GetTransactionByIdUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.UpdateTransactionUseCase
import com.budget.buddy.domain.cash.usecase.cashtransaction.UpdateTransactionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CashTransactionModule {

    @Singleton
    @Provides
    fun provideCashTransactionRepository(dao: CashTransactionDao): CashTransactionsRepository {
        return CashTransactionRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideAddCashTransactionUseCase(repository: CashTransactionsRepository): AddCashTransactionUseCase {
        return AddCashTransactionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTransactionsByIdsUseCase(repository: CashTransactionsRepository): DeleteTransactionsByIdsUseCase {
        return DeleteTransactionsByIdsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTransactionUseCase(repository: CashTransactionsRepository): DeleteTransactionUseCase {
        return DeleteTransactionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllTransactionsUseCase(repository: CashTransactionsRepository): GetAllTransactionsUseCase {
        return GetAllTransactionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTransactionByIdUseCase(repository: CashTransactionsRepository): GetTransactionByIdUseCase {
        return GetTransactionByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateTransactionsUseCase(repository: CashTransactionsRepository): UpdateTransactionsUseCase {
        return UpdateTransactionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateTransactionUseCase(repository: CashTransactionsRepository): UpdateTransactionUseCase {
        return UpdateTransactionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideTime(dao: TimeDao): WorkTime {
        return WorkTime(dao)
    }
}