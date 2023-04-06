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

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

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
    postValue(listOf(ErrorStateViewEntity(action = retryAction, error = error)))
}

fun MutableStateFlow<List<CastcleViewEntity>?>.error(
    error: Throwable?,
    retryAction: () -> Unit
) {
    value = listOf(ErrorStateViewEntity(action = retryAction, error = error))
}

fun RecyclerView.findFirstVisibleItemPosition(isCompletelyVisible: Boolean = true): Int {
    val layoutManager = layoutManager as? LinearLayoutManager
    return if (isCompletelyVisible) {
        layoutManager?.findFirstCompletelyVisibleItemPosition() ?: -1
    } else {
        layoutManager?.findFirstVisibleItemPosition() ?: -1
    }
}

suspend fun RecyclerView.forceScrollToTop() {
    if (findFirstVisibleItemPosition() == 0) {
        delay(69)
        return
    }
    while (findFirstVisibleItemPosition() != 0) {
        scrollToPosition(0)
        delay(69)
    }
}

fun MutableLiveData<List<CastcleViewEntity>>.loading() {
    postValue(listOf(LoadingViewEntity()))
}

fun MutableStateFlow<List<CastcleViewEntity>?>.loading() {
    value = listOf(LoadingViewEntity())
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