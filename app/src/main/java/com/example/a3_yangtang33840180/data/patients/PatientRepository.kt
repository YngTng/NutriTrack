package com.example.a3_yangtang33840180.data.patients

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PatientRepository(context: Context, scope: CoroutineScope) {
    private val patientDAO: PatientDAO = PatientDatabase.getDatabase(context).patientDao()

    suspend fun insertPatient(patient: Patient) {
        patientDAO.insertPatient(patient)
    }

    suspend fun insertPatients(patients: List<Patient>) {
        patientDAO.insertPatients(patients)
    }

    suspend fun updatePatient(patient: Patient) {
        patientDAO.updatePatient(patient)
    }

    suspend fun deletePatient(patient: Patient) {
        patientDAO.deletePatient(patient)
    }

    suspend fun deletePatientById(patientId: Int) {
        patientDAO.deletePatientById(patientId)
    }

    suspend fun deleteAllPatients() {
        patientDAO.deleteAllPatients()
    }

    fun getAllPatients(): Flow<List<Patient>> = patientDAO.getAllPatients()
}