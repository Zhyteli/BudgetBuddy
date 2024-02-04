package com.budget.buddy.di.module

import com.budget.buddy.data.database.CashTransactionDao
import com.budget.buddy.data.impl.CashTransactionRepositoryImpl
import com.budget.buddy.domain.cash.repository.CashTransactionsRepository
import com.budget.buddy.domain.cash.usecase.AddCashTransactionUseCase
import com.budget.buddy.domain.cash.usecase.DeleteTransactionUseCase
import com.budget.buddy.domain.cash.usecase.DeleteTransactionsByIdsUseCase
import com.budget.buddy.domain.cash.usecase.GetAllTransactionsUseCase
import com.budget.buddy.domain.cash.usecase.GetTransactionByIdUseCase
import com.budget.buddy.domain.cash.usecase.UpdateTransactionUseCase
import com.budget.buddy.domain.cash.usecase.UpdateTransactionsUseCase
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
}