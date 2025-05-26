package com.example.a3_yangtang33840180.data.foodIntake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.a3_yangtang33840180.data.patients.Patient


@Entity(
    tableName = "foodintakes",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["patientId"])]
)
@TypeConverters(Converters::class)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientId: Int,
    val selectedFoods: List<String> = emptyList(),
    val sleepTime: String = "00:00",
    val wakeTime: String = "00:00",
    val biggestMealTime: String = "00:00",
    val selectedPersona: String? = null
)