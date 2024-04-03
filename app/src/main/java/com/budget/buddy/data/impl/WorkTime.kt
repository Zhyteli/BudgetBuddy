package com.budget.buddy.data.impl

import com.budget.buddy.data.database.TimeDao
import com.budget.buddy.domain.user.Time
import javax.inject.Inject

class WorkTime @Inject constructor(
    private val timeDao:TimeDao
) {
    fun getWorkTime() = timeDao.getTime()
    fun setWorkTime(time: Time) = timeDao.saveTime(time)
}