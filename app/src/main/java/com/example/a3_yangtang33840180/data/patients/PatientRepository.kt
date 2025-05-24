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

    suspend fun getPatientById(id: Int): Patient? {
        return patientDao.getPatientById(id)
    }

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    suspend fun getAverageHeifaScores(): Pair<Double, Double> {
        val maleAvg = patientDao.getAverageTotalHeifaScoreMale() ?: 0.0
        val femaleAvg = patientDao.getAverageTotalHeifaScoreFemale() ?: 0.0
        return Pair(maleAvg, femaleAvg)
    }

    companion object {
        @Volatile
        private var INSTANCE: PatientRepository? = null

        fun getInstance(context: Context): PatientRepository {
            return INSTANCE ?: synchronized(this) {
                val database = PatientDatabase.getDatabase(context)
                val instance = PatientRepository(database.patientDao())
                INSTANCE = instance
                instance
            }
        }
    }
}