package com.example.a3_yangtang33840180.data.foodIntake

class FoodIntakeRepository(private val foodIntakeDao: FoodIntakeDao) {

    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insertFoodIntake(foodIntake)
    }

    suspend fun update(foodIntake: FoodIntake) {
        foodIntakeDao.updateFoodIntake(foodIntake)
    }

    suspend fun delete(foodIntake: FoodIntake) {
        foodIntakeDao.deleteFoodIntake(foodIntake)
    }

    suspend fun getFoodIntakeByUserId(userId: Int): FoodIntake? {
        return foodIntakeDao.getFoodIntakeByUserId(userId)
    }

    suspend fun getAllFoodIntakes(): List<FoodIntake> {
        return foodIntakeDao.getAllFoodIntakes()
    }
}