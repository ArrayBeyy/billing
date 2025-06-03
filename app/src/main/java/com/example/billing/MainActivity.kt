package com.example.billing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billing.api.config.RetrofitClient
import com.example.billing.api.model.VoucherResponse
import okhttp3.MultipartBody
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
            //val code = VoucherRepository.postVoucher("BCWXXS")
            //val code = VoucherRepository.postVoucher("CWAZ8R")

            /*
                RetrofitClient.instance.readVoucher(code.toString()).enqueue(object : Callback<VoucherResponse> {
                    override fun onResponse(call: Call<VoucherResponse>, response: Response<VoucherResponse>) {
                        System.out.println("[RESP] "+response.body()?.message)
                        if (response.body()?.message == "true") {
                            startActivity(Intent(this@MainActivity, WhatsAppActivity::class.java))
                        } else {
                            Toast.makeText(this@MainActivity, "Voucher tidak valid", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Gagal koneksi API", Toast.LENGTH_SHORT).show()
                    }
                })
            */

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("code_voucher", etVoucher.text.toString())

            RetrofitClient.instance.readVoucher(builder.build())
                .enqueue(object : Callback<VoucherResponse> {
                    override fun onResponse(
                        call: Call<VoucherResponse>,
                        response: Response<VoucherResponse>
                    ) {
                        if (response.body()?.message == "Voucher berhasil terbaca") {
                            Toast.makeText(applicationContext, "${response.body()?.voucher?.id} ${response.body()?.voucher?.code} ${response.body()?.voucher?.customer_name}", Toast.LENGTH_SHORT).show()
                            //startActivity(Intent(this@MainActivity, WhatsAppActivity::class.java))
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Voucher tidak valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Gagal koneksi API: "+t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}


