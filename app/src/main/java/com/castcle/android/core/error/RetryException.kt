package com.castcle.android.core.error

import androidx.paging.LoadState

class RetryException(
    val error: Throwable,
    val retryAction: (() -> Unit)? = null,
) : Throwable(error) {

    companion object {
        fun loadState(error: Throwable, retryAction: (() -> Unit)? = null) = LoadState.Error(
            RetryException(
                error = error,
                retryAction = retryAction,
            )
        )
    }

}