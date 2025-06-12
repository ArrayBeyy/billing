package com.example.billing

import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.billing.api.config.RetrofitClient
import com.example.billing.api.model.VoucherResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimerOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private var timer: CountDownTimer? = null
    private var secondsLeft = 120 // contoh timer 60 detik
    private var codeVoucher = "";


    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE ) as WindowManager
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.activity_timer_overlay_service, null)

        val timerText = overlayView.findViewById<TextView>(R.id.timerText)
        val stopButton = overlayView.findViewById<Button>(R.id.stopButton)

        startTimer(timerText)

        stopButton.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val current = LocalDateTime.now().format(formatter)

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("code_voucher", codeVoucher)
            builder.addFormDataPart("time_stop", current)

            RetrofitClient.instance.stopVoucher(builder.build())
                .enqueue(object : Callback<VoucherResponse> {
                    override fun onResponse(
                        call: Call<VoucherResponse>,
                        response: Response<VoucherResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.message == "Voucher berhasil distop") {
                                timer?.cancel()
                                val mainIntent = Intent(this@TimerOverlayService, MainActivity::class.java)
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(mainIntent)
                                stopSelf()
                            }
                        }
                        else {
                            Toast.makeText(applicationContext, "Gagal stop voucher", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                        Toast.makeText(this@TimerOverlayService, "Gagal koneksi API: "+t.message, Toast.LENGTH_SHORT).show()
                    }
                })
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

            @SuppressLint("NewApi")
            override fun onFinish() {
                timerText.text = "Selesai!"

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val current = LocalDateTime.now().format(formatter)

                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                builder.addFormDataPart("code_voucher", codeVoucher)
                builder.addFormDataPart("time_stop", current)

                RetrofitClient.instance.stopVoucher(builder.build())
                    .enqueue(object : Callback<VoucherResponse> {
                        override fun onResponse(
                            call: Call<VoucherResponse>,
                            response: Response<VoucherResponse>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.message == "Voucher berhasil distop") {
                                    val backToMain = Intent(this@TimerOverlayService, MainActivity::class.java)
                                    backToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(backToMain)
                                }
                            }
                            else {
                                Toast.makeText(applicationContext, "Gagal stop voucher", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                            Toast.makeText(this@TimerOverlayService, "Gagal koneksi API: "+t.message, Toast.LENGTH_SHORT).show()
                        }
                    })

                // Balik ke aplikasi utama
                val mainIntent = Intent(this@TimerOverlayService, MainActivity::class.java)
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            codeVoucher = intent.getStringExtra("CODE_VOUCHER") ?: ""
            secondsLeft = intent.getIntExtra("DURATION", 0)

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("code_voucher", codeVoucher)

            RetrofitClient.instance.useVoucher(builder.build())
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
