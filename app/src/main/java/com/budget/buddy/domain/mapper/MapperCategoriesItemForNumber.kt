package com.budget.buddy.domain.mapper

import com.budget.buddy.presentation.ui.categories.CategoryIcons

object MapperCategoriesItemForNumber {
    fun mapOfNumber(categoriesItem: String): Int {
        return when (categoriesItem) {
            CategoryIcons.FOOD.icons -> CategoryIcons.FOOD.code
            CategoryIcons.RESTAURANT.icons -> CategoryIcons.RESTAURANT.code
            CategoryIcons.MEDICINE.icons -> CategoryIcons.MEDICINE.code
            CategoryIcons.ENTERTAINMENT.icons -> CategoryIcons.ENTERTAINMENT.code
            CategoryIcons.BEAUTY.icons -> CategoryIcons.BEAUTY.code
            CategoryIcons.HOME.icons -> CategoryIcons.HOME.code
            CategoryIcons.PETS.icons -> CategoryIcons.PETS.code
            CategoryIcons.CLOTHES.icons -> CategoryIcons.CLOTHES.code
            CategoryIcons.SPORT.icons -> CategoryIcons.SPORT.code
            CategoryIcons.TRANSPORT.icons -> CategoryIcons.TRANSPORT.code
            CategoryIcons.TAXI.icons -> CategoryIcons.TAXI.code
            CategoryIcons.TRAVEL.icons -> CategoryIcons.TRAVEL.code
            CategoryIcons.OTHER.icons -> CategoryIcons.OTHER.code
            else -> CategoryIcons.OTHER.code
        }
    }
    fun mapOfCategoriesItem(number: Int): CategoryIcons {
        return when (number) {
            CategoryIcons.FOOD.code -> CategoryIcons.FOOD
            CategoryIcons.RESTAURANT.code -> CategoryIcons.RESTAURANT
            CategoryIcons.MEDICINE.code -> CategoryIcons.MEDICINE
            CategoryIcons.ENTERTAINMENT.code -> CategoryIcons.ENTERTAINMENT
            CategoryIcons.BEAUTY.code -> CategoryIcons.BEAUTY
            CategoryIcons.HOME.code -> CategoryIcons.HOME
            CategoryIcons.PETS.code -> CategoryIcons.PETS
            CategoryIcons.CLOTHES.code -> CategoryIcons.CLOTHES
            CategoryIcons.SPORT.code -> CategoryIcons.SPORT
            CategoryIcons.TRANSPORT.code -> CategoryIcons.TRANSPORT
            CategoryIcons.TAXI.code -> CategoryIcons.TAXI
            CategoryIcons.TRAVEL.code -> CategoryIcons.TRAVEL
            CategoryIcons.OTHER.code -> CategoryIcons.OTHER
            else -> CategoryIcons.OTHER
        }
    }
}