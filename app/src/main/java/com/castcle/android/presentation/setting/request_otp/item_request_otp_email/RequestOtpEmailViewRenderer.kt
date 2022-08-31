package com.castcle.android.presentation.setting.request_otp.item_request_otp_email

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemRequestOtpEmailBinding
import com.castcle.android.presentation.setting.request_otp.RequestOtpListener
import io.reactivex.disposables.CompositeDisposable

class RequestOtpEmailViewRenderer : CastcleViewRenderer<RequestOtpEmailViewEntity,
    RequestOtpEmailViewHolder,
    RequestOtpListener>(R.layout.item_request_otp_email) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: RequestOtpListener,
        compositeDisposable: CompositeDisposable
    ) = RequestOtpEmailViewHolder(
        ItemRequestOtpEmailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}