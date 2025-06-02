package com.example.billing.api

import com.example.billing.api.model.VoucherRespoonse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

    interface ApiService {
        @POST("api/read/voucher")
        fun readVoucher(@Body code: String): Call<VoucherRespoonse>

    }
