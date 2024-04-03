package com.budget.buddy.domain.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Time(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String
)
