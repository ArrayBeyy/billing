package com.example.billing

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class TimerOverlayActivity : AppCompatActivity() {

    private lateinit var timerViewModel: TimerViewModel
    private lateinit var overlayLayout: FrameLayout
    private lateinit var timerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_overlay)

        overlayLayout = findViewById(R.id.overlayLayout)
        timerText = findViewById(R.id.timerText)

        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        timerViewModel.startTimer(30*1000)
        timerViewModel.timeLeft.observe(this) { time ->
            timerText.text = time
            if (time == "Expired") {
                overlayLayout.visibility = View.GONE
            }
        }
    }
}