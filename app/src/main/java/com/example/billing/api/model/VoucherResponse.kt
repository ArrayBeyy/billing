package com.example.billing.api.model

data class VoucherResponse(
    val message: String,
    val voucher: Voucher
)

data class Voucher(
    val code: String,
    val customer_name: String,
    val duration: Int = 0,
    val id: Int = 0
)