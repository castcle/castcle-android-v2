/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.castcle.android.R

fun Activity.color(@ColorRes id: Int?) = if (id != null) {
    ContextCompat.getColor(this, id)
} else {
    Color.WHITE
}

fun Activity.colorStateList(@ColorRes id: Int?) = if (id != null) {
    ContextCompat.getColorStateList(this, id)
} else {
    ColorStateList.valueOf(Color.WHITE)
}

fun Activity.dimenDp(@DimenRes id: Int): Float = resources.getDimension(id)

fun Activity.dimenPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)

fun Activity.drawable(@DrawableRes id: Int?): Drawable? = ResourcesCompat.getDrawable(
    resources,
    id ?: R.drawable.ic_back,
    null
)

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

inline fun <reified T : Activity> AppCompatActivity.navigate(
    finishCurrent: Boolean = false,
    callback: ActivityResultLauncher<Intent>? = null,
    intentBody: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply(intentBody)
    if (callback != null) callback.launch(intent) else startActivity(intent)
    if (finishCurrent) finishAfterTransition()
}

fun Activity.openUrl(url: String?) {
    url ?: return
    val mapUrl = if (url.trim().startsWith("https://")) {
        url.trim()
    } else {
        "https://${url.trim()}"
    }
    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(mapUrl)))
}

fun Activity.screenWidthPx(): Int {
    val displayMetrics = resources.displayMetrics
    return displayMetrics.widthPixels
}

fun Activity.string(@StringRes id: Int?): String =
    getString(id ?: R.string.app_name)

fun Activity.toast(message: String?) {
    Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
}