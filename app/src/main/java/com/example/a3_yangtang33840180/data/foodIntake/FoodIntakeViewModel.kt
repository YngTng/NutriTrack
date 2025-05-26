package com.example.a3_yangtang33840180.data.foodIntake

// QuestionnaireViewModel.kt
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3_yangtang33840180.data.patients.PatientDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// FoodIntakeViewModel.kt
class FoodIntakeViewModel(
    private val userId: Int, // Now Int
    private val repository: FoodIntakeRepository
) : ViewModel() {

    private val _foodIntake = MutableStateFlow<FoodIntake?>(null)
    val foodIntake: StateFlow<FoodIntake?> = _foodIntake

    init {
        loadFoodIntake()
    }

    private fun loadFoodIntake() {
        viewModelScope.launch {
            val existing = repository.getFoodIntakeByUserId(userId)
            if (existing != null) {
                _foodIntake.value = existing
            } else {
                // Create new empty record if none exists
                val newFoodIntake = FoodIntake(
                    patientId = userId, // userId is Int
                    selectedFoods = emptyList(),
                    sleepTime = "22:00",
                    wakeTime = "06:00",
                    biggestMealTime = "12:00",
                    selectedPersona = "Health Devotee"
                )
                repository.upsertFoodIntake(newFoodIntake)
                _foodIntake.value = newFoodIntake
            }
        }
    }

    fun updateFoodCategories(selectedCategories: List<String>) {
        val current = _foodIntake.value ?: return
        val updated = current.copy(selectedFoods = selectedCategories)
        saveFoodIntake(updated)
    }

    fun updateSleepTime(time: String) {
        val current = _foodIntake.value ?: return
        val updated = current.copy(sleepTime = time)
        saveFoodIntake(updated)
    }

    fun updateWakeTime(time: String) {
        val current = _foodIntake.value ?: return
        val updated = current.copy(wakeTime = time)
        saveFoodIntake(updated)
    }

    fun updateBiggestMealTime(time: String) {
        val current = _foodIntake.value ?: return
        val updated = current.copy(biggestMealTime = time)
        saveFoodIntake(updated)
    }

    fun updatePersona(persona: String) {
        val current = _foodIntake.value ?: return
        val updated = current.copy(selectedPersona = persona)
        saveFoodIntake(updated)
    }

    private fun saveFoodIntake(foodIntake: FoodIntake) {
        _foodIntake.value = foodIntake
        viewModelScope.launch {
            try {
                repository.upsertFoodIntake(foodIntake)
            } catch (e: Exception) {
                // Log or show error
            }
        }
    }
}