package com.example.a3_yangtang33840180.data.genAI

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachTipDAO {

    @Insert
    suspend fun insertNutriCoachTip(nutricoachtip: NutriCoachTip)

    @Update
    suspend fun updateNutriCoachTip(nutricoachtip: NutriCoachTip)

    @Delete
    suspend fun deleteNutriCoachTip(nutricoachtip: NutriCoachTip)

    @Query("DELETE FROM nutricoachtips")
    suspend fun deleteAllNutriCoachTips()

    @Query("SELECT * FROM nutricoachtips ORDER BY id ASC")
    fun getAllNutriCoachTips(): Flow<List<NutriCoachTip>>
}