package com.example.billing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.iwgang.countdownview.CountdownView
import com.example.billing.api.config.RetrofitClient
import com.example.billing.api.model.VoucherResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CountdownActivity : AppCompatActivity() {

    lateinit var mCountdownView: CountdownView

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        val codeVoucher = intent.getStringExtra("CODE_VOUCHER")!!

        mCountdownView = findViewById(R.id.countdownView)
        mCountdownView.start(10*1000)
        mCountdownView.setOnCountdownEndListener {
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
                                val backToMain = Intent(this@CountdownActivity, MainActivity::class.java)
                                backToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(backToMain)
                            }
                        }
                        else {
                            Toast.makeText(applicationContext, "Gagal stop voucher", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                        Toast.makeText(this@CountdownActivity, "Gagal koneksi API: "+t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}