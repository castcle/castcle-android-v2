package com.castcle.android.presentation.setting.verify_password.item_verify_password

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemVerifyPasswordBinding
import com.castcle.android.presentation.setting.verify_password.VerifyPasswordListener
import io.reactivex.disposables.CompositeDisposable

class VerifyPasswordViewRenderer : CastcleViewRenderer<VerifyPasswordViewEntity,
    VerifyPasswordViewHolder,
    VerifyPasswordListener>(R.layout.item_verify_password) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: VerifyPasswordListener,
        compositeDisposable: CompositeDisposable
    ) = VerifyPasswordViewHolder(
        ItemVerifyPasswordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}