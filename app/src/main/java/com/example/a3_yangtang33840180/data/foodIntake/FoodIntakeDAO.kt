package com.example.a3_yangtang33840180.data.foodIntake

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Update
    suspend fun updateFoodIntake(foodIntake: FoodIntake)

    @Delete
    suspend fun deleteFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM foodIntakes WHERE userId = :userId LIMIT 1")
    suspend fun getFoodIntakeByUserId(userId: Int): FoodIntake?

    @Query("SELECT * FROM foodIntakes")
    suspend fun getAllFoodIntakes(): List<FoodIntake>
}