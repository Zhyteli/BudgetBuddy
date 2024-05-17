package com.budget.buddy.domain.ai

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AiAnalysis(
    @PrimaryKey(autoGenerate = true)val id: Int,
    val month: Int,
    val year: Int,
    val analysis: String
)
