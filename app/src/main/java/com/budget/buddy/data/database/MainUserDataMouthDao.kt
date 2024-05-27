package com.budget.buddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.budget.buddy.domain.user.MainUserDataMouth

@Dao
interface MainUserDataMouthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDataMainUserDataMouth(mainUser:MainUserDataMouth)
    @Query("SELECT * FROM MainUserDataMouth")
    suspend fun loadDataMainUserDataMouth(): MainUserDataMouth
    @Query("DELETE FROM MainUserDataMouth")
    suspend fun deleteDataMainUserDataMouth()
}