package com.budget.buddy.data.impl

import com.budget.buddy.data.database.AiAnalysisDao
import com.budget.buddy.domain.ai.AiAnalysis
import com.budget.buddy.domain.cash.repository.AiAnalysisRepository
import javax.inject.Inject

class AiAnalysisRepositoryImpl @Inject constructor(
    private val dao: AiAnalysisDao
): AiAnalysisRepository {
    override suspend fun saveAnalyzeTransactions(promt: AiAnalysis) {
        dao.savePromt(promt)
    }

    override suspend fun createPrompt(): AiAnalysis {
        return dao.getPromt()
    }
}