package com.budget.buddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.budget.buddy.domain.user.MainUserDataMouth

@Dao
interface MainUserDataMouthDao {
    @Insert
    suspend fun saveDataMainUserDataMouth(mainUser:MainUserDataMouth)
    @Query("SELECT * FROM MainUserDataMouth")
    suspend fun loadDataMainUserDataMouth(): MainUserDataMouth
}