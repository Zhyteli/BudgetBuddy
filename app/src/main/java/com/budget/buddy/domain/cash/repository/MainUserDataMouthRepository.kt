package com.budget.buddy.domain.cash.repository

import com.budget.buddy.domain.user.MainUserDataMouth

interface MainUserDataMouthRepository {
    suspend fun saveDataMainUserDataMouth(mainUser: MainUserDataMouth)
    suspend fun loadDataMainUserDataMouth(): MainUserDataMouth?
}