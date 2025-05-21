package com.example.a3_yangtang33840180.fruityVice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FruitViewModel : ViewModel() {
    private val _fruit = MutableStateFlow<Fruit?>(null)
    val fruit: StateFlow<Fruit?> = _fruit

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchFruit(name: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val result = ApiClient.api.getFruit(name.lowercase())
                _fruit.value = result
            } catch (e: Exception) {
                _error.value = "Fruit not found or network error."
                _fruit.value = null
            }
        }
    }
}