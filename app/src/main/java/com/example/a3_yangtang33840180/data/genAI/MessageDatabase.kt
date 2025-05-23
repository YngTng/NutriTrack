package com.example.a3_yangtang33840180.data.genAI

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDAO

    companion object {
        @Volatile
        private var Instance: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MessageDatabase::class.java, "message_database")
                    .build().also { Instance = it }
            }
        }
    }
}