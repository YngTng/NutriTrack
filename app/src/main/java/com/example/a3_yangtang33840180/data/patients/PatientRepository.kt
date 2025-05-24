package com.example.a3_yangtang33840180.data.patients

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PatientRepository(private val patientDao: PatientDAO) {

    suspend fun insertPatient(patient: Patient) {
        patientDao.insertPatient(patient)
    }

    suspend fun insertPatients(patients: List<Patient>) {
        patientDao.insertPatients(patients)
    }

    suspend fun updatePatient(patient: Patient) {
        patientDao.updatePatient(patient)
    }

    suspend fun deletePatient(patient: Patient) {
        patientDao.deletePatient(patient)
    }

    suspend fun deletePatientById(patientId: Int) {
        patientDao.deletePatientById(patientId)
    }

    suspend fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    suspend fun getPatientById(id: Int): Patient? {
        return patientDao.getPatientById(id)
    }

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()
}