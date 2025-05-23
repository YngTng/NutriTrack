package com.example.a3_yangtang33840180.data.patients

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PatientViewModel(context: Context): ViewModel() {
    private val patientRepository: PatientRepository = PatientRepository(context)
    val allPatients: Flow<List<Patient>> = patientRepository.getAllPatients()

    fun insertPatient(patient: Patient) = viewModelScope.launch {
        patientRepository.insertPatient(patient)
    }

    fun updatePatient(patient: Patient) = viewModelScope.launch {
        patientRepository.updatePatient(patient)
    }

    fun deletePatient(patient: Patient) = viewModelScope.launch {
        patientRepository.deletePatient(patient)
    }

    fun deletePatientById(patientId: Int) = viewModelScope.launch {
        patientRepository.deletePatientById(patientId)
    }

    fun deleteAllPatients() = viewModelScope.launch {
        patientRepository.deleteAllPatients()
    }

    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientViewModel(context) as T
    }

}