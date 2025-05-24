package com.example.a3_yangtang33840180.data.genAI

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NutriCoachTipViewModel(context: Context): ViewModel() {
    private val nutricoachtipRepository: NutriCoachTipRepository = NutriCoachTipRepository(context)
    val allNutriCoachTips: Flow<List<NutriCoachTip>> = nutricoachtipRepository.getAllMessages()

    fun insertNutriCoachTip(message: NutriCoachTip) = viewModelScope.launch {
        nutricoachtipRepository.insertMessage(message)
    }

    fun deleteAllNutriCoachTips() = viewModelScope.launch {
        nutricoachtipRepository.deleteAllMessages()
    }

    class NutriCoachTipViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NutriCoachTipViewModel(context) as T
    }

}