package com.example.a3_yangtang33840180.data.foodIntake

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a3_yangtang33840180.data.patients.PatientViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodIntakeViewModel(private val foodIntakeRepository: FoodIntakeRepository) : ViewModel() {

    private val _foodIntake = MutableStateFlow<FoodIntake?>(null)
    val foodIntake: StateFlow<FoodIntake?> = _foodIntake

    fun loadFoodIntake(userId: Int) {
        viewModelScope.launch {
            val f = foodIntakeRepository.getFoodIntakeByUserId(userId)
            _foodIntake.value = f
        }
    }

    fun insertOrUpdateFoodIntake(foodIntake: FoodIntake) {
        viewModelScope.launch {
            foodIntakeRepository.insert(foodIntake)
            _foodIntake.value = foodIntake
        }
    }

    class PatientViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatientViewModel(context.applicationContext) as T
        }
    }
}