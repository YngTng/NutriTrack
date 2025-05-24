package com.example.a3_yangtang33840180.data.genAI

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutricoachtips")
class NutriCoachTip (
    /**
     * id
     * name
     * ranking
     * topVenue: favourite venue
     */

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val theNutriCoachTip: String
)