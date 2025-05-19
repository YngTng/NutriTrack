package com.example.a3_yangtang33840180.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDietData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDietDataDao(): UserDataDao
}