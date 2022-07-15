package com.castcle.android.core.custom_view.load_state

import com.castcle.android.core.base.recyclerview.CastcleListener

interface LoadStateListener : CastcleListener {
    fun onRefreshClicked() = Unit
    fun onRetryClicked() = Unit
}