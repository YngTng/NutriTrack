package com.example.a3_yangtang33840180

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3_yangtang33840180.data.patients.PatientRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: PatientRepository) : ViewModel() {
    fun validateLogin(
        userId: String,
        password: String,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val userIdInt = userId.toIntOrNull()
            if (userIdInt == null) {
                onError("Invalid User ID")
                return@launch
            }

            try {
                val isValid = repository.getPatientById(userIdInt)?.passWord == password
                if (isValid) {
                    onSuccess(userIdInt)
                } else {
                    onError("Invalid ID or password")
                }
            } catch (e: Exception) {
                onError("Login failed: ${e.message}")
            }
        }
    }
}