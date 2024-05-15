package com.budget.buddy.domain.mapper.convert

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

object ConvertTime {
    fun convertTimestampToDate(timestamp: Int): String {
        val sdf = SimpleDateFormat("M")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(timestamp.toLong() * 1000))
    }

    fun convertDateToTimestamp(dateString: String): Long {
        val sdf = SimpleDateFormat("MM")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(dateString)
        return date.time / 1000
    }
}