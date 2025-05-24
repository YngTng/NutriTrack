package com.example.a3_yangtang33840180.data.foodIntake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.a3_yangtang33840180.data.foodIntake.Converters
import com.example.a3_yangtang33840180.data.patients.Patient

@Entity(
    tableName = "foodIntakes",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["userId"]
        )
    ]
)
@TypeConverters(Converters::class)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val biggestMealTime: String,
    val sleepTime: String,
    val wakeUpTime: String,
    val persona: String,
    val foodCategories: List<String>
)