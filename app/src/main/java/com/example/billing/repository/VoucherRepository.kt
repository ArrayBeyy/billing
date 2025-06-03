package com.example.billing.repository

import com.example.billing.api.config.RetrofitClient
import com.example.billing.api.model.VoucherRequest
import com.example.billing.api.model.VoucherResponse
import com.example.billing.util.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VoucherRepository {
    fun postVoucher(voucherCode: String) {
        /*
        val request = VoucherRequest(voucherCode.trim())

        val call = RetrofitClient.instance.readVoucher(request.code)
        call.enqueue(object : Callback<VoucherResponse> {
            override fun onResponse(
                call: Call<VoucherResponse>,
                response: Response<VoucherResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Logger.i("API_SUCCESS", "Message: ${body?.message}, Voucher: ${body?.voucher}")
                } else {
                    Logger.e("API_ERROR", "HTTP ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                Logger.e("API_FAILURE", "Exception: ${t.message}")
            }
        })

         */
    }
}