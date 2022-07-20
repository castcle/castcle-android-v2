package com.castcle.android.core.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewEntity

fun RecyclerView.ViewHolder.color(@ColorRes id: Int?) = if (id != null) {
    ContextCompat.getColor(itemView.context, id)
} else {
    Color.WHITE
}

fun RecyclerView.ViewHolder.colorStateList(@ColorRes id: Int?) = if (id != null) {
    ContextCompat.getColorStateList(itemView.context, id)
} else {
    ColorStateList.valueOf(Color.WHITE)
}

fun RecyclerView.ViewHolder.context(): Context = itemView.context

fun RecyclerView.ViewHolder.dimenDp(@DimenRes id: Int): Float =
    itemView.context.resources.getDimension(id)

fun RecyclerView.ViewHolder.dimenPx(@DimenRes id: Int): Int =
    itemView.context.resources.getDimensionPixelSize(id)

fun RecyclerView.ViewHolder.drawable(@DrawableRes id: Int?): Drawable? =
    ResourcesCompat.getDrawable(
        itemView.context.resources,
        id ?: R.drawable.ic_back,
        null
    )

fun MutableLiveData<List<CastcleViewEntity>>.error(error: Throwable?, retryAction: () -> Unit) {
    postValue(listOf(ErrorStateViewEntity(error = error, retryAction = retryAction)))
}

fun RecyclerView.firstVisibleItemPosition(): Int {
    return layoutManager
        ?.cast<LinearLayoutManager>()
        ?.findFirstVisibleItemPosition()
        ?: Int.MAX_VALUE
}

fun MutableLiveData<List<CastcleViewEntity>>.loading() {
    postValue(listOf(LoadingViewEntity()))
}

fun RecyclerView.ViewHolder.screenWidthPx(): Int {
    return itemView.context.resources.displayMetrics.widthPixels
}

fun SwipeRefreshLayout.setRefreshColor(
    @ColorRes backgroundColor: Int,
    @ColorRes iconColor: Int,
) {
    setProgressBackgroundColorSchemeColor(context.getColor(backgroundColor))
    setColorSchemeColors(context.getColor(iconColor))
}

fun RecyclerView.ViewHolder.string(@StringRes id: Int?): String =
    itemView.context.getString(id ?: R.string.app_name)

fun RecyclerView.ViewHolder.toast(message: String?) {
    Toast.makeText(itemView.context, "$message", Toast.LENGTH_LONG).show()
}