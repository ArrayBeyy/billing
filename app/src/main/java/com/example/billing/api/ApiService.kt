package com.example.billing.api

import com.example.billing.api.model.UseVoucherResponse
import com.example.billing.api.model.VoucherResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    //@POST("api/read/voucher")
    //fun readVoucher(@Body code: String): Call<VoucherResponse>

    @POST("api/read/voucher")
    fun readVoucher(@Body reqBody: RequestBody): Call<VoucherResponse>

    @POST("api/use/voucher")
    fun useVoucher(@Body reqBody: RequestBody): Call<UseVoucherResponse>

    @POST("api/stop/voucher")
    fun stopVoucher(@Body reqBody: RequestBody): Call<VoucherResponse>
}
