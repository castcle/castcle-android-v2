package com.castcle.android.core.extensions

import android.app.Dialog
import android.widget.Toast
import androidx.annotation.StringRes
import com.castcle.android.R

fun Dialog.string(@StringRes id: Int?): String =
    context.getString(id ?: R.string.app_name)

fun Dialog.toast(message: String?) {
    Toast.makeText(context, "$message", Toast.LENGTH_LONG).show()
}