package com.budget.buddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.budget.buddy.domain.ai.AiAnalysis

@Dao
interface AiAnalysisDao {

    @Query("SELECT * FROM AiAnalysis")
    suspend fun getPromt(): AiAnalysis

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePromt(promt: AiAnalysis)
}