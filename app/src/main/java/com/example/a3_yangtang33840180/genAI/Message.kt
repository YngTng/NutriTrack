package com.example.a3_yangtang33840180.genAI

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
class Message (
    /**
     * id
     * name
     * ranking
     * topVenue: favourite venue
     */

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val theMessage: String
)