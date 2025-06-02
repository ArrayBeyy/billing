package com.example.billing

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TimerOverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var timer: CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        overlayView = LayoutInflater.from(this).inflate(R.layout.activity_timer_overlay_service, null)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.addView(overlayView, params)

        val tvTimer = overlayView!!.findViewById<TextView>(R.id.timerText)
        val btnStop = overlayView!!.findViewById<Button>(R.id.btnStop)

        timer = object : CountDownTimer(600000, 1000) { // 10 minutes
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                stopSelf()
            }
        }.start()

        btnStop.setOnClickListener {
            stopSelf()
            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        windowManager?.removeView(overlayView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
