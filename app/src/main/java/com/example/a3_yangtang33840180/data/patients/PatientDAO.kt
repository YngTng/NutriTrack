package com.example.a3_yangtang33840180.data.patients

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PatientDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<Patient>)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE userId = :id")
    suspend fun getPatientById(id: Int): Patient?

    @Query("DELETE FROM patients WHERE userId = :patientId")
    suspend fun deletePatientById(patientId: Int)

    @Query("DELETE FROM patients")
    suspend fun deleteAllPatients()
}