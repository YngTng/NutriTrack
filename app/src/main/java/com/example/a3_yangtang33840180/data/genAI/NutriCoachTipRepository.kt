package com.example.a3_yangtang33840180.data.genAI

import android.content.Context
import kotlinx.coroutines.flow.Flow

class NutriCoachTipRepository {
    var nutricoachtipDAO: NutriCoachTipDAO

    constructor(context: Context) {
        nutricoachtipDAO = NutriCoachTipDatabase.getDatabase(context).nutricoachtipDao()
    }

    suspend fun insertMessage(message: NutriCoachTip) {
        nutricoachtipDAO.insertNutriCoachTip(message)
    }

    suspend fun deleteAllMessages() {
        nutricoachtipDAO.deleteAllNutriCoachTips()
    }

    fun getAllMessages(): Flow<List<NutriCoachTip>> = nutricoachtipDAO.getAllNutriCoachTips()

}