package com.example.a3_yangtang33840180.data.foodIntake

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoodIntakeViewModelFactory(
    private val userId: Int,
    private val repository: FoodIntakeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodIntakeViewModel::class.java)) {
            return FoodIntakeViewModel(userId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}