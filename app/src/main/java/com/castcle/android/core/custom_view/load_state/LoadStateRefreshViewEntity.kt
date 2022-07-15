package com.castcle.android.core.custom_view.load_state

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.error.RetryException

interface LoadStateRefreshViewEntity : CastcleViewEntity {

    fun error(onRefresh: () -> Unit, onRetry: () -> Unit, throwable: Throwable?) {
        if (throwable is RetryException) {
            action = throwable.retryAction
            error = throwable.error
        } else {
            error = throwable
        }
        refreshAction = onRefresh
        retryAction = onRetry
    }

    fun clear() {
        action = null
        error = null
    }

    var action: (() -> Unit)?

    var refreshAction: () -> Unit

    var retryAction: () -> Unit

    var error: Throwable?

}