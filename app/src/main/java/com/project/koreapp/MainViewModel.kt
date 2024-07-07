package com.project.koreapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _isBlackScreen = MutableLiveData<Boolean>()
    val isBlackScreen: LiveData<Boolean> = _isBlackScreen

    fun showBlackScreen() {
        _isBlackScreen.value = true
    }

    fun hideBlackScreen() {
        _isBlackScreen.value = false
    }

}