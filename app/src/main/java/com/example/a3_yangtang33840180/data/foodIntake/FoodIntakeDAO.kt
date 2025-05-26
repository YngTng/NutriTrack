package com.example.a3_yangtang33840180.data.foodIntake

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodIntakeDao {

    @Query("SELECT * FROM foodintakes WHERE patientId = :userId LIMIT 1")
    suspend fun getFoodIntakeByUserId(userId: Int): FoodIntake?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(foodIntake: FoodIntake)
}