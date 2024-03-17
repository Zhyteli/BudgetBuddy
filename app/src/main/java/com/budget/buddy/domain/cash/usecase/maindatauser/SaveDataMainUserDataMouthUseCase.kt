package com.budget.buddy.domain.cash.usecase.maindatauser

import com.budget.buddy.domain.cash.repository.MainUserDataMouthRepository
import com.budget.buddy.domain.user.MainUserDataMouth

class SaveDataMainUserDataMouthUseCase(private val repository: MainUserDataMouthRepository) {
    suspend operator fun invoke(month: MainUserDataMouth) {
        repository.saveDataMainUserDataMouth(month)
    }
}