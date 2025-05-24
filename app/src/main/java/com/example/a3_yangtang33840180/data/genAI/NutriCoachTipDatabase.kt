package com.example.a3_yangtang33840180.data.genAI

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NutriCoachTip::class], version = 1, exportSchema = false)
abstract class NutriCoachTipDatabase : RoomDatabase() {
    abstract fun nutricoachtipDao(): NutriCoachTipDAO

    companion object {
        @Volatile
        private var Instance: NutriCoachTipDatabase? = null

        fun getDatabase(context: Context): NutriCoachTipDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NutriCoachTipDatabase::class.java,
                    "nutricoachtips_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}