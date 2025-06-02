package com.example.billing.util

import android.util.Log

object Logger {
    private const val DEFAULT_TAG = "BillingApp"

    fun d(tag: String = DEFAULT_TAG, message: String) {
        Log.d(tag, message)
    }

    fun i(tag: String = DEFAULT_TAG, message: String) {
        Log.i(tag, message)
    }

    fun e(tag: String = DEFAULT_TAG, message: String) {
        Log.e(tag, message)
    }

    fun w(tag: String = DEFAULT_TAG, message: String) {
        Log.w(tag, message)
    }
}