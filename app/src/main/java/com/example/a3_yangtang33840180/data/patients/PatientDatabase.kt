package com.example.a3_yangtang33840180.data.patients

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a3_yangtang33840180.data.foodIntake.Converters
import com.example.a3_yangtang33840180.data.foodIntake.FoodIntake
import com.example.a3_yangtang33840180.data.foodIntake.FoodIntakeDao

@Database(entities = [Patient::class, FoodIntake::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PatientDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDAO
    abstract fun foodIntakeDao(): FoodIntakeDao

    companion object {
        @Volatile
        private var INSTANCE: PatientDatabase? = null

        fun getDatabase(context: Context): PatientDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatientDatabase::class.java,
                    "patient_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}