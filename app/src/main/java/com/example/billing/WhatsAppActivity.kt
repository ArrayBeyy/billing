package com.example.billing

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.billing.api.config.RetrofitClient
import com.example.billing.api.model.UseVoucherResponse
import com.example.billing.api.model.VoucherResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WhatsAppActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_app)

        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val codeVoucher = intent.getStringExtra("CODE_VOUCHER")

        btnSend.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 1234)
            } else {

                val phone = etPhone.text.toString()
                if (phone.isNotEmpty()) {


                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val current = LocalDateTime.now().format(formatter)

                    val builder = MultipartBody.Builder()
                    builder.setType(MultipartBody.FORM)
                    builder.addFormDataPart("code_voucher", codeVoucher!!)
                    builder.addFormDataPart("start_time", current)
                    builder.addFormDataPart("phone_number", etPhone.text.toString())

                    RetrofitClient.instance.useVoucher(builder.build())
                        .enqueue(object : Callback<UseVoucherResponse> {
                            override fun onResponse(
                                call: Call<UseVoucherResponse>,
                                response: Response<UseVoucherResponse>
                            ) {
                                if (response.isSuccessful) {
                                    if (response.body()?.message == "Voucher berhasil digunakan") {
                                        val url = "https://wa.me/$phone"
                                        val intentOverlay = Intent(this@WhatsAppActivity, TimerOverlayService::class.java)
                                        intentOverlay.putExtra("CODE_VOUCHER", codeVoucher)
                                        intentOverlay.putExtra("DURATION", response.body()?.voucher?.duration)
                                        startService(intentOverlay)
                                        val intentWA = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        startActivity(intentWA)
                                    }
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Cek apakah no HP sudah terdaftar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<UseVoucherResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@WhatsAppActivity,
                                    "Gagal koneksi API: " + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

            }
        }
    }
}