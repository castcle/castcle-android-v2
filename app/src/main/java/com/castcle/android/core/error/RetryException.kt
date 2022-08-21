package com.castcle.android.core.error

import androidx.paging.LoadState
import com.castcle.android.core.base.recyclerview.CastcleViewEntity

class RetryException(
    val action: (() -> Unit)? = null,
    val error: Throwable? = null,
    val errorItems: List<CastcleViewEntity>? = null,
) : Throwable(error?.message) {

    companion object {
        fun loadState(
            error: Throwable? = null,
            errorItems: List<CastcleViewEntity>? = null,
            retryAction: () -> Unit = {},
        ) = LoadState.Error(
            RetryException(
                action = retryAction,
                error = error,
                errorItems = errorItems,
            )
        )
    }

}