package com.castcle.android.presentation.setting.verify_otp.item_verify_otp

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemVerifyOtpBinding
import com.castcle.android.presentation.setting.verify_otp.VerifyOtpListener
import io.reactivex.disposables.CompositeDisposable

class VerifyOtpViewRenderer : CastcleViewRenderer<VerifyOtpViewEntity,
    VerifyOtpViewHolder,
    VerifyOtpListener>(R.layout.item_verify_otp) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: VerifyOtpListener,
        compositeDisposable: CompositeDisposable
    ) = VerifyOtpViewHolder(
        ItemVerifyOtpBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}