package com.example.a3_yangtang33840180.data.patients

import android.content.Context
import kotlinx.coroutines.flow.Flow

class PatientRepository {
    var patientDAO: PatientDAO

    constructor(context: Context) {
        patientDAO = PatientDatabase.getDatabase(context).patientDao()
    }

    suspend fun insertPatient(patient: Patient) {
        patientDAO.insertPatient(patient)
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