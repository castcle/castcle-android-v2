package com.castcle.android.core.custom_view.load_state.item_error_state

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemErrorStateBinding
import io.reactivex.disposables.CompositeDisposable

class ErrorStateViewRenderer : CastcleViewRenderer<ErrorStateViewEntity,
    ErrorStateViewHolder,
    CastcleListener>(R.layout.item_error_state) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = ErrorStateViewHolder(
        ItemErrorStateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable
    )

}