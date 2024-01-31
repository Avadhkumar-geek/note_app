package com.example.roomhandson.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class AppToast {
    fun showToast(msg: String, context: Context): Toast {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG)
    }

    fun showSnackBar(msg: String, view: View): Snackbar {
        return Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
    }
}