package com.budget.buddy.domain.mapper.convert

import android.content.Context
import java.util.Locale

fun getSystemLanguage(context: Context): String {
    val locale: Locale = context.resources.configuration.locales.get(0)
    return locale.language
}
