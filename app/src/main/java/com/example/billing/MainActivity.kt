package com.example.billing

import android.content.Context
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
    //bagian screen pining
    /*private var tapCount = 0
    private var lastTapTime = 0L*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        //screen pining
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            if (!activityManager.isInLockTaskMode) {
                startLockTask()
                Toast.makeText(this, "Screen pinning aktif", Toast.LENGTH_SHORT).show()
            }
        }*/

        etVoucher = findViewById(R.id.etVoucher)
        btnCheck = findViewById(R.id.btnCheck)

        btnCheck.setOnClickListener {
            //val cd = Intent(this@MainActivity, CountdownActivity::class.java)
            //startActivity(cd)

            // Sebelum membuka WhatsApp
            //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
             //   stopLockTask() // Lepaskan screen pinning secara otomatis
            //}

            // Deteksi 5x ketukan cepat untuk keluar
           /* val currentTime = System.currentTimeMillis()
            if (currentTime - lastTapTime < 1000) {
                tapCount++
                if (tapCount >= 5) {
                    stopLockTask()
                    Toast.makeText(this, "Screen pinning dimatikan", Toast.LENGTH_SHORT).show()
                    tapCount = 0
                }
            } else {
                tapCount = 1
            }
            lastTapTime = currentTime?*/


            if (etVoucher.text.toString().isEmpty()){
                Toast.makeText(applicationContext, "Kode voucher belum di-input.", Toast.LENGTH_SHORT).show()
            }
            else {
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                builder.addFormDataPart("code_voucher", etVoucher.text.toString())

                RetrofitClient.instance.readVoucher(builder.build())
                    .enqueue(object : Callback<VoucherResponse> {
                        override fun onResponse(
                            call: Call<VoucherResponse>,
                            response: Response<VoucherResponse>
                        ) {
                            if (response.isSuccessful){
                                if (response.body()?.message == "Voucher berhasil terbaca") {
                                    //Toast.makeText(applicationContext, "${response.body()?.voucher?.id} ${response.body()?.voucher?.code} ${response.body()?.voucher?.customer_name}", Toast.LENGTH_SHORT).show()
                                    val nextIntent = Intent(this@MainActivity, WhatsAppActivity::class.java)
                                    nextIntent.putExtra("CODE_VOUCHER", etVoucher.text.toString())
                                    startActivity(nextIntent)
                                }
                            }
                            else {
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
}


