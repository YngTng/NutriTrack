package com.example.a3_yangtang33840180.data.patients

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PatientViewModel(context: Context) : ViewModel() {
    private val patientRepository: PatientRepository = PatientRepository(
        PatientDatabase.getDatabase(context).patientDao()
    )

    val allPatients: Flow<List<Patient>> = patientRepository.getAllPatients()

    fun insertPatient(patient: Patient) = viewModelScope.launch {
        patientRepository.insertPatient(patient)
    }

    fun updatePatient(patient: Patient) = viewModelScope.launch {
        patientRepository.updatePatient(patient)
    }

    class PatientViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatientViewModel(context.applicationContext) as T
        }
    }
}