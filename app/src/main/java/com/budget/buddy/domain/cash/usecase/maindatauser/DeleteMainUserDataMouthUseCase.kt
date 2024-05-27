package com.budget.buddy.domain.cash.usecase.maindatauser

import com.budget.buddy.domain.cash.repository.MainUserDataMouthRepository

class DeleteMainUserDataMouthUseCase(
    private val repository: MainUserDataMouthRepository,
) {
    suspend operator fun invoke() = repository.deleteDataMainUserDataMouth()
}