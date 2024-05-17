package com.budget.buddy.domain.cash.repository

import com.budget.buddy.domain.ai.AiAnalysis

interface AiAnalysisRepository {
    suspend fun saveAnalyzeTransactions(promt: AiAnalysis)
    suspend fun createPrompt(): AiAnalysis
}