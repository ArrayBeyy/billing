package com.example.billing

import android.content.Context
import android.widget.Toast

object Utils {
    fun showError(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
