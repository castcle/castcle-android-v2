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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.constants.AUTHORIZATION_PREFIX
import com.castcle.android.core.constants.HEADER_AUTHORIZATION
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.data.base.BaseUiState
import com.facebook.FacebookSdk.getCacheDir
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.twitter.sdk.android.core.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.Flow
import okhttp3.HttpUrl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import timber.log.Timber
import java.io.*
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.*

suspend fun <T> apiCall(apiCall: suspend () -> Response<T>): T? {
    try {
        val response = apiCall()
        val responseBody = response.body()
        if (response.isSuccessful) {
            return responseBody
        } else {
            throw ErrorMapper().map(response.errorBody())
        }
    } catch (exception: Exception) {
        throw ErrorMapper().map(exception)
    }
}

suspend fun <T> toApiCallFlow(call: suspend () -> Response<T>): Flow<BaseUiState<T>> {
    return flow {
        emit(BaseUiState.Loading(null, true))

        try {
            call().let { c ->
                try {
                    if (c.isSuccessful) {
                        c.body()?.let {
                            emit(BaseUiState.Success(it))
                        } ?: emit(BaseUiState.SuccessNonBody)
                    } else {
                        emit(BaseUiState.Error(ErrorMapper().map(c.errorBody())))
                    }
                } catch (e: Exception) {
                    emit(BaseUiState.Error(ErrorMapper().map(c.errorBody())))
                }
            }
            emit(BaseUiState.Loading(null, false))
        } catch (e: Exception) {
            emit(BaseUiState.Error(ErrorMapper().map(call().errorBody())))
        }

    }.flowOn(Dispatchers.IO)
}

fun Int?.asCount(): String {
    val number = if (this == null || this < 0) 0 else this
    val format = NumberFormat.getInstance().apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }
    return when (number) {
        in 0..999 -> "$number"
        in 1_000..999_999 -> "${format.format(number.div(1_000.0))}K"
        in 1_000_000..999_999_999 -> "${format.format(number.div(1_000_000.0))}M"
        else -> "${format.format(number.div(1_000_000_000.0))}G"
    }
}

fun Double?.asCount(): String {
    val number = if (this == null || this < 0.0) 0.0 else this
    val format = NumberFormat.getInstance().apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }
    return when (number) {
        in 0.0..999.9999999999999 -> format.format(number)
        in 1_000.0..999_999.9999999999999 -> "${format.format(number.div(1_000.0))}K"
        in 1_000_000.0..999_999_999.9999999999999 -> "${format.format(number.div(1_000_000.0))}M"
        else -> "${format.format(number.div(1_000_000_000.0))}G"
    }
}

fun Double.asCastToken(): String {
    return NumberFormat.getInstance()
        .apply { maximumFractionDigits = 8 }
        .format(this)
        .plus(" CAST")
}

fun Int.asComma(): String {
    return NumberFormat.getInstance().format(this)
}

inline fun <reified T> byKoin(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

inline fun <reified T> Any.cast(action: (T) -> Unit = {}) = try {
    (this as? T)?.apply(action)
} catch (exception: Exception) {
    Timber.e("${exception.message}")
    null
}

fun convertPxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

fun convertDpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun Context.copyToClipboard(text: String?): Boolean {
    return try {
        val clipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clipData = ClipData.newPlainText("", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
        true
    } catch (e: Exception) {
        false
    }
}

fun Iterable<String?>.filterNotNullOrBlank(): List<String> {
    return filterNotNull().filter { it.isNotBlank() }
}

inline fun <reified T : Fragment> AppCompatActivity.findFragmentInNavHost(): T? {
    return supportFragmentManager.findFragmentById(R.id.navHostContainer)
        ?.childFragmentManager
        ?.fragments
        ?.find { it is T } as? T?
}

fun getCurrentVisibleFragment(fragmentManager: FragmentManager): Fragment? {
    return fragmentManager.fragments.find {
        it?.isVisible == true
    }
}

@SuppressLint("HardwareIds")
fun Context.getDeviceUniqueId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        ?: UUID.randomUUID().toString()
}

fun View.gone() {
    visibility = View.GONE
}

fun User.getLargeProfileImageUrlHttps(): String? {
    return if (profileImageUrlHttps?.endsWith("_normal.jpg") == true) {
        profileImageUrlHttps.replace("_normal.jpg", ".jpg")
    } else {
        profileImageUrlHttps
    }
}

@Suppress("DEPRECATION")
fun getScreenHeight(activity: Activity): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = activity.windowManager.currentWindowMetrics
        val insets =
            windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

fun getStatusBarHeight(activity: Activity): Int {
    val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) activity.resources.getDimensionPixelSize(resourceId)
    else Rect().apply { activity.window.decorView.getWindowVisibleDisplayFrame(this) }.top
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun <reified T> mapListObject(raw: Any?): List<T> {
    return raw?.cast<List<*>>()?.mapNotNull { mapObject<T>(it) }.orEmpty()
}

inline fun <reified T> mapObject(raw: Any?): T? {
    raw ?: return null
    return try {
        Gson().fromJson(Gson().toJsonTree(raw).asJsonObject, T::class.java)
    } catch (exception: Exception) {
        Timber.e("${exception.message}")
        null
    }
}

fun View.onClick(delay: Int = 500, action: (view: View) -> Unit): Disposable =
    RxView.clicks(this)
        .throttleFirst(delay.toLong(), TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            action.invoke(this)
        }, {
            Timber.e("${it.message}")
            Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
        })

fun TextView.onTextChange(delay: Int = 0, action: (String) -> Unit): Disposable =
    RxTextView.textChanges(this)
        .skipInitialValue()
        .debounce(delay.toLong(), TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            action.invoke(it.toString())
        }, {
            Timber.e("${it.message}")
            Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
        })

fun <T> List<T>.secondOrNull(): T? {
    return if (size < 2) null else this[1]
}

fun Bitmap.saveImageToCache(context: Context): Uri {
    return try {
        val imagesFolder = File(getCacheDir(), "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "shared_image.png")
        val stream = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.flush()
        stream.close()
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file)
    } catch (exception: IOException) {
        throw exception
    }
}

fun Activity.saveImage(bitmap: Bitmap, name: String) {
    val folderName = "Castcle"
    val fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$folderName")
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        resolver.openOutputStream(imageUri ?: Uri.EMPTY)
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .toString()
            .plus(File.separator)
            .plus(folderName)
        val file = File(imagesDir)
        if (!file.exists()) {
            file.mkdir()
        }
        val image = File(imagesDir, "$name.png")
        FileOutputStream(image)
    }
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    fos?.flush()
    fos?.close()
}

fun Activity.shareImageUri(uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.type = "image/png"
    startActivity(intent)
}

fun View.setPadding(
    @DimenRes top: Int? = null,
    @DimenRes bottom: Int? = null,
    @DimenRes start: Int? = null,
    @DimenRes end: Int? = null,
) {
    setPadding(
        start?.let { resources.getDimensionPixelSize(it) } ?: paddingLeft,
        top?.let { resources.getDimensionPixelSize(it) } ?: paddingTop,
        end?.let { resources.getDimensionPixelSize(it) } ?: paddingRight,
        bottom?.let { resources.getDimensionPixelSize(it) } ?: paddingBottom,
    )
}

suspend fun timer(
    delay: Long = 1_000,
    delayCount: Int? = null,
    delayOnStart: Long? = null,
) = flow {
    delay(delayOnStart ?: delay)
    var count = 0
    while (delayCount?.let { count < it } != false) {
        emit(++count)
        delay(delay)
    }
}

fun String.toBearer(url: HttpUrl? = null): String {
    if (BuildConfig.DEBUG && url != null) {
        Timber.d("$HEADER_AUTHORIZATION($url) : $AUTHORIZATION_PREFIX$this")
    }
    return "$AUTHORIZATION_PREFIX$this"
}

fun View.toBitmap(
    onBeforeDrawBitmap: () -> Unit = {},
    onError: (Exception) -> Unit = {},
    onSuccess: (Bitmap) -> Unit = {},
) {
    onBeforeDrawBitmap.invoke()
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            try {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onSuccess.invoke(drawToBitmap())
            } catch (exception: Exception) {
                onError.invoke(exception)
            }
        }
    })
    requestLayout()
}

fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

@Suppress("DEPRECATION")
fun Context.vibrate(time: Int = 50) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(Context.VIBRATOR_MANAGER_SERVICE).cast<VibratorManager>()?.defaultVibrator
    } else {
        getSystemService(Context.VIBRATOR_SERVICE).cast<Vibrator>()
    }
    vibrator?.vibrate(
        VibrationEffect.createOneShot(time.toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
    )
}

fun Context.getColorResource(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun View.visible() {
    visibility = View.VISIBLE
}