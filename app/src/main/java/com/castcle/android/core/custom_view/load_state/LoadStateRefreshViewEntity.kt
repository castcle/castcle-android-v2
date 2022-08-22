package com.castcle.android.core.custom_view.load_state

import com.castcle.android.core.base.recyclerview.CastcleViewEntity

interface LoadStateRefreshViewEntity : CastcleViewEntity {

    var action: () -> Unit

    var error: Throwable?

    fun updateItem(updateAction: () -> Unit, updateError: Throwable?) {
        action = updateAction
        error = updateError
    }

}