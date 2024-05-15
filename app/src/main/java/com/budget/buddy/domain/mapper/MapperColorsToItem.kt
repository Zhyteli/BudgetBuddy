package com.budget.buddy.domain.mapper

import androidx.compose.ui.graphics.Color
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.presentation.ui.categories.CategoryIcons

object MapperColorsToItem {
    fun mapToColor(item: SpendingItem): Color {
        return when (item.imageResourceId) {
            CategoryIcons.FOOD.icons -> Color(0xFFE57373)
            CategoryIcons.RESTAURANT.icons -> Color(0xFF81C784)
            CategoryIcons.MEDICINE.icons -> Color(0xFF64B5F6)
            CategoryIcons.BEAUTY.icons -> Color(0xFFFFD54F)
            CategoryIcons.HOME.icons -> Color(0xFF9575CD)
            CategoryIcons.PETS.icons -> Color(0xFF4DB6AC)
            CategoryIcons.TAXI.icons -> Color(0xFFAED581)
            CategoryIcons.TRAVEL.icons -> Color(0xFF4DD0E1)
            CategoryIcons.TRANSPORT.icons -> Color(0xFFFF8A65)
            CategoryIcons.CLOTHES.icons -> Color(0xFF90A4AE)
            CategoryIcons.ENTERTAINMENT.icons -> Color(0xFFA1887F)
            CategoryIcons.SPORT.icons -> Color(0xFFFFA54F)
            CategoryIcons.OTHER.icons -> Color(0xFF77584D)
            else -> Color(0xFFE57373)
        }
    }

    fun colorToCategory(color: Color): CategoryIcons {
        return when (color) {
            Color(0xFFE57373) -> CategoryIcons.FOOD
            Color(0xFF81C784) -> CategoryIcons.RESTAURANT
            Color(0xFF64B5F6) -> CategoryIcons.MEDICINE
            Color(0xFFFFD54F) -> CategoryIcons.BEAUTY
            Color(0xFF9575CD) -> CategoryIcons.HOME
            Color(0xFF4DB6AC) -> CategoryIcons.PETS
            Color(0xFFAED581) -> CategoryIcons.TAXI
            Color(0xFF4DD0E1) -> CategoryIcons.TRAVEL
            Color(0xFFFF8A65) -> CategoryIcons.TRANSPORT
            Color(0xFF90A4AE) -> CategoryIcons.CLOTHES
            Color(0xFFA1887F) -> CategoryIcons.ENTERTAINMENT
            Color(0xFFFFA54F) -> CategoryIcons.SPORT
            else -> CategoryIcons.OTHER
        }
    }
}