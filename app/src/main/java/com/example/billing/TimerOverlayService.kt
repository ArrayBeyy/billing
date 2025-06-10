package com.example.billing

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class TimerOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private var timer: CountDownTimer? = null
    private var secondsLeft = 120 // contoh timer 60 detik

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE ) as WindowManager
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.activity_timer_overlay_service, null)

        val timerText = overlayView.findViewById<TextView>(R.id.timerText)
        val stopButton = overlayView.findViewById<Button>(R.id.stopButton)

        startTimer(timerText)

        stopButton.setOnClickListener {
            timer?.cancel()
            val mainIntent = Intent(this, WhatsAppActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainIntent)
            stopSelf()
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100
        windowManager.addView(overlayView, params)
    }

    private fun startTimer(timerText: TextView) {
        timer = object : CountDownTimer((secondsLeft * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerText.text = "Timer: $seconds"
            }

            override fun onFinish() {
                timerText.text = "Selesai!"

                // Balik ke aplikasi utama
                val mainIntent = Intent(this@TimerOverlayService, WhatsAppActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(mainIntent)

                // Tutup overlay dan stop service
                stopSelf()
            }
        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
        fun onBind(intent: Intent?): IBinder? = null
    }
}
