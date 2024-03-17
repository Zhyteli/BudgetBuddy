package com.budget.buddy.data.impl

import com.budget.buddy.data.database.MainUserDataMouthDao
import com.budget.buddy.domain.cash.repository.MainUserDataMouthRepository
import com.budget.buddy.domain.user.MainUserDataMouth
import javax.inject.Inject

class MainUserDataMouthRepositoryImpl @Inject constructor(
    private val mainUserDataMouthDao: MainUserDataMouthDao
): MainUserDataMouthRepository {
    override suspend fun saveDataMainUserDataMouth(mainUser:MainUserDataMouth) {
        mainUserDataMouthDao.saveDataMainUserDataMouth(mainUser)
    }

    override suspend fun loadDataMainUserDataMouth(): MainUserDataMouth{
        return mainUserDataMouthDao.loadDataMainUserDataMouth()
    }
}