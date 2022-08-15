package com.castcle.android.core.extensions

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.*
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.constants.AUTHORIZATION_PREFIX
import com.castcle.android.core.constants.HEADER_AUTHORIZATION
import com.castcle.android.core.error.ApiException
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.twitter.sdk.android.core.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.HttpUrl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import timber.log.Timber
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
            throw ApiException.map(response.errorBody())
        }
    } catch (exception: Exception) {
        throw exception
    }
}

fun Int?.asCount(): String {
    val number = if (this == null || this < 0) 0 else this
    val format = NumberFormat.getInstance().apply {
        maximumFractionDigits = 1
        minimumFractionDigits = 0
    }
    return when (number) {
        in 0..999 -> "$number"
        in 1_000..999_999 -> "${format.format(number.div(1_000.0))}K"
        in 1_000_000..999_999_999 -> "${format.format(number.div(1_000_000.0))}M"
        else -> "${format.format(number.div(1_000_000_000.0))}G"
    }
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
        val clipboardManager = this
            .getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clipData = ClipData
            .newPlainText("feed-message", text)
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

fun String.toBearer(url: HttpUrl? = null): String {
    if (BuildConfig.DEBUG && url != null) {
        Timber.d("$HEADER_AUTHORIZATION($url) : $AUTHORIZATION_PREFIX$this")
    }
    return "$AUTHORIZATION_PREFIX$this"
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

fun View.visible() {
    visibility = View.VISIBLE
}