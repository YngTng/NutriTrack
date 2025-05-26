package com.example.a3_yangtang33840180.data.foodIntake

import kotlinx.coroutines.flow.Flow

class FoodIntakeRepository(private val foodIntakeDao: FoodIntakeDao) {

    // Returns a single instance (suspend function)
    suspend fun getFoodIntakeByUserId(userId: Int): FoodIntake? {
        return foodIntakeDao.getFoodIntakeByUserId(userId)
    }

    // Insert or update FoodIntake record
    suspend fun upsertFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.upsert(foodIntake)
    }
}