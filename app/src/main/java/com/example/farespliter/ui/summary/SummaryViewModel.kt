package com.example.farespliter.ui.summary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.farespliter.data.AppDatabase
import com.example.farespliter.data.model.Friend
import com.example.farespliter.repository.RideRepository
import kotlinx.coroutines.launch

class SummaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RideRepository =
        RideRepository(AppDatabase.getInstance(application))

    private val _owedAmounts = MutableLiveData<Map<Friend, Double>>()
    val owedAmounts: LiveData<Map<Friend, Double>> = _owedAmounts

    fun loadSummary() {
        viewModelScope.launch {
            val amounts = repository.calculateOwedAmounts()
            _owedAmounts.value = amounts
        }
    }
}