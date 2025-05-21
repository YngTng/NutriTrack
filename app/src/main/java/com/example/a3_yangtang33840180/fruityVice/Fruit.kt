package com.example.a3_yangtang33840180.fruityVice

data class Fruit(
    val name: String,
    val genus: String,
    val family: String,
    val order: String,
    val nutritions: Nutrition
)

data class Nutrition(
    val carbohydrates: Float,
    val protein: Float,
    val fat: Float,
    val calories: Int,
    val sugar: Float
)