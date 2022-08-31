package com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemRequestOtpMobileBinding
import com.castcle.android.presentation.setting.request_otp.RequestOtpListener
import io.reactivex.disposables.CompositeDisposable

class RequestOtpMobileViewRenderer : CastcleViewRenderer<RequestOtpMobileViewEntity,
    RequestOtpMobileViewHolder,
    RequestOtpListener>(R.layout.item_request_otp_mobile) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: RequestOtpListener,
        compositeDisposable: CompositeDisposable
    ) = RequestOtpMobileViewHolder(
        ItemRequestOtpMobileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}