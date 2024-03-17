package com.budget.buddy.di.module

import com.budget.buddy.data.database.MainUserDataMouthDao
import com.budget.buddy.data.impl.MainUserDataMouthRepositoryImpl
import com.budget.buddy.domain.cash.repository.MainUserDataMouthRepository
import com.budget.buddy.domain.cash.usecase.maindatauser.LoadDataMainUserDataMouthUseCase
import com.budget.buddy.domain.cash.usecase.maindatauser.SaveDataMainUserDataMouthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainUserDataMouthModule {

    @Singleton
    @Provides
    fun provideMainUserDataMouthRepository(
        mainUserDataMouthDao: MainUserDataMouthDao,
    ): MainUserDataMouthRepository {
        return MainUserDataMouthRepositoryImpl(mainUserDataMouthDao)
    }

    @Singleton
    @Provides
    fun provideSaveDataMainUserDataMouthUseCase(
        mainUserDataMouthRepository: MainUserDataMouthRepository
    ): SaveDataMainUserDataMouthUseCase {
        return SaveDataMainUserDataMouthUseCase(mainUserDataMouthRepository)
    }

    @Singleton
    @Provides
    fun provideLoadDataMainUserDataMouthUseCase(
        mainUserDataMouthRepository: MainUserDataMouthRepository
    ): LoadDataMainUserDataMouthUseCase {
        return LoadDataMainUserDataMouthUseCase(mainUserDataMouthRepository)
    }
}