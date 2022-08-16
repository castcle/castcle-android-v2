package com.castcle.android.core.extensions

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.castcle.android.R

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