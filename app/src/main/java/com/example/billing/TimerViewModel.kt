package com.example.billing

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    val timeLeft = MutableLiveData<String>()
    private var countDownTimer: CountDownTimer? = null

    fun startTimer(expiryTime: Long) {
        val currentTime = System.currentTimeMillis()
        val millisUntilFinished = expiryTime - currentTime

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val h = seconds / 3600
                val m = (seconds % 3600) / 60
                val s = seconds % 60
                timeLeft.postValue(String.format("%02d:%02d:%02d", h, m, s))
            }

            override fun onFinish() {
                timeLeft.postValue("Expired")
            }
        }.start()
    }

    override fun onCleared() {
        countDownTimer?.cancel()
        super.onCleared()
    }
}