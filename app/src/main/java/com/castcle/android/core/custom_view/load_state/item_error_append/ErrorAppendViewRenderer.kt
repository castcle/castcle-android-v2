package com.castcle.android.core.custom_view.load_state.item_error_append

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemErrorAppendBinding
import io.reactivex.disposables.CompositeDisposable

class ErrorAppendViewRenderer : CastcleViewRenderer<ErrorAppendViewEntity,
    ErrorAppendViewHolder,
    CastcleListener>(R.layout.item_error_append) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = ErrorAppendViewHolder(
        ItemErrorAppendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable
    )

}