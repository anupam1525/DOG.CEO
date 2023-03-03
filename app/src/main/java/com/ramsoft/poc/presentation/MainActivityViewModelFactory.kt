package com.ramsoft.poc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramsoft.poc.repository.DogRepository

class MainActivityViewModelFactory(private val repository: DogRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}