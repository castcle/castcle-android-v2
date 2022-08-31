package com.castcle.android.presentation.setting.request_otp.item_request_otp_email

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemRequestOtpEmailBinding
import com.castcle.android.presentation.setting.request_otp.RequestOtpListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class RequestOtpEmailViewHolder(
    private val binding: ItemRequestOtpEmailBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RequestOtpListener,
) : CastcleViewHolder<RequestOtpEmailViewEntity>(binding.root) {

    override var item = RequestOtpEmailViewEntity()

    init {
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onRequestOtp(
                countryCode = "",
                email = item.email,
                mobileNumber = "",
            )
        }
    }

    override fun bind(bindItem: RequestOtpEmailViewEntity) {
        binding.tvEmail.text = item.email
    }

}