package com.example.billing.api.model

data class VoucherRespoonse(
    val message: String,
    val voucher: Voucher
)

data class Voucher(
    val code: String,
    val customer_name: String,
    val duration: Boolean = true,
    val id: Int = 0
)