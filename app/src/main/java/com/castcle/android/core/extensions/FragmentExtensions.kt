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
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.castcle.android.R
import timber.log.Timber

@Suppress("DEPRECATION")
inline fun <reified T> Fragment.arguments(key: String?): T? = try {
    when (T::class) {
        Boolean::class -> arguments?.getBoolean(key) as? T
        Float::class -> arguments?.getFloat(key) as? T
        Int::class -> arguments?.getInt(key) as? T
        Long::class -> arguments?.getLong(key) as? T
        Parcelable::class -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(key, T::class.java)
        } else {
            arguments?.getParcelable(key)
        }
        String::class -> arguments?.getString(key) as? T
        else -> null
    }
} catch (throwable: Throwable) {
    Timber.e(throwable)
    null
}

@Suppress("DEPRECATION")
fun Fragment.changeSoftInputMode(isResize: Boolean) {
    if (isResize) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    } else {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
}

fun Fragment.color(@ColorRes id: Int?) = if (id != null) {
    ContextCompat.getColor(requireContext(), id)
} else {
    Color.WHITE
}

fun Fragment.colorStateList(@ColorRes id: Int?) = if (id != null) {
    ContextCompat.getColorStateList(requireContext(), id)
} else {
    ColorStateList.valueOf(Color.WHITE)
}

fun Fragment.dimenDp(@DimenRes id: Int): Float = resources.getDimension(id)

fun Fragment.dimenPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)

fun Fragment.drawable(@DrawableRes id: Int?): Drawable? = ResourcesCompat.getDrawable(
    resources,
    id ?: R.drawable.ic_back,
    null
)

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Fragment.isKeyboardVisible(binding: ViewBinding): Boolean {
    return ViewCompat.getRootWindowInsets(binding.root)
        ?.isVisible(WindowInsetsCompat.Type.ime()) == true
}

inline fun <reified T : Activity> Fragment.navigate(
    finishCurrent: Boolean = false,
    callback: ActivityResultLauncher<Intent>? = null,
    intentBody: Intent.() -> Unit = {}
) {
    val intent = Intent(requireContext(), T::class.java).apply(intentBody)
    if (callback != null) callback.launch(intent) else startActivity(intent)
    if (finishCurrent) activity?.finishAfterTransition()
}

fun Fragment.openUrl(url: String?) {
    url ?: return
    val mapUrl = if (url.trim().startsWith("https://")) {
        url.trim()
    } else {
        "https://${url.trim()}"
    }
    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(mapUrl)))
}

fun Fragment.screenWidthPx(): Int {
    val displayMetrics = requireContext().resources.displayMetrics
    return displayMetrics.widthPixels
}

fun Fragment.string(@StringRes id: Int?): String =
    requireContext().getString(id ?: R.string.app_name)

fun Fragment.toast(message: String?) {
    Toast.makeText(requireContext(), "$message", Toast.LENGTH_LONG).show()
}