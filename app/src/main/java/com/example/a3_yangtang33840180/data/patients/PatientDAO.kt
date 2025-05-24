package com.example.a3_yangtang33840180.data.patients

import androidx.room.Dao
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

    @Query("SELECT COUNT(*) FROM patients")
    suspend fun getPatientCount(): Int

    @Update
    suspend fun updatePatient(patient: Patient)

    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE userId = :id")
    suspend fun getPatientById(id: Int): Patient?

    @Query("SELECT AVG(heifaTotalScoreMale) FROM patients")
    suspend fun getAverageTotalHeifaScoreMale(): Double?

    @Query("SELECT AVG(heifaTotalScoreFemale) FROM patients")
    suspend fun getAverageTotalHeifaScoreFemale(): Double?
}