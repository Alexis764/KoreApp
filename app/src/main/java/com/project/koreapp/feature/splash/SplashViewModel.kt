package com.project.koreapp.feature.splash

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _startNextScreen = MutableLiveData<Boolean>()
    val startNextScreen: LiveData<Boolean> = _startNextScreen

    private val _milliseconds = MutableLiveData<Long>()
    val milliseconds: LiveData<Long> = _milliseconds

    private val splashCountDownTimer: CountDownTimer = object : CountDownTimer(3500, 500) {
        override fun onTick(milliseconds: Long) {
            _milliseconds.value = milliseconds
        }

        override fun onFinish() {
            _startNextScreen.value = true
        }
    }.start()

}