package com.example.billing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.billing.api.ApiService
import com.example.billing.api.config.RetrofitClient
import com.example.billing.api.model.Voucher
import com.example.billing.api.model.VoucherRequest
import com.example.billing.api.model.VoucherRespoonse
import com.example.billing.repository.VoucherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {
    private lateinit var etVoucher: EditText
    private lateinit var btnCheck: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etVoucher = findViewById(R.id.etVoucher)
        btnCheck = findViewById(R.id.btnCheck)

        btnCheck.setOnClickListener {
            val code = VoucherRepository.postVoucher("BCWXXS")
                RetrofitClient.instance.readVoucher(code.toString()).enqueue(object : Callback<VoucherRespoonse> {
                    override fun onResponse(call: Call<VoucherRespoonse>, response: Response<VoucherRespoonse>) {
                        if (response.body()?.message == "true") {
                            startActivity(Intent(this@MainActivity, WhatsAppActivity::class.java))
                        } else {
                            Toast.makeText(this@MainActivity, "Voucher tidak valid", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<VoucherRespoonse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Gagal koneksi API", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
}


