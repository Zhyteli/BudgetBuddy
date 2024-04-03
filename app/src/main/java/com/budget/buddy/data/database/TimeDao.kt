package com.budget.buddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.budget.buddy.domain.user.Time

@Dao
interface TimeDao {
    @Query("SELECT * FROM Time")
    fun getTime(): Time?
    @Insert
    fun saveTime(time: Time)
}