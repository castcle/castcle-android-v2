package com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile

import android.annotation.SuppressLint
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemRequestOtpMobileBinding
import com.castcle.android.presentation.setting.request_otp.RequestOtpListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class RequestOtpMobileViewHolder(
    private val binding: ItemRequestOtpMobileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RequestOtpListener
) : CastcleViewHolder<RequestOtpMobileViewEntity>(binding.root) {

    override var item = RequestOtpMobileViewEntity()

    init {
        compositeDisposable += binding.tvCountryCode.onClick {
            listener.onMobileCountryCodeClicked()
        }
        compositeDisposable += binding.etMobileNumber.onTextChange {
            item.mobileNumber = it
            updateConfirmButton()
        }
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onRequestOtp(
                countryCode = item.countryCode.dialCode,
                email = "",
                mobileNumber = item.mobileNumber,
            )
        }
    }

    override fun bind(bindItem: RequestOtpMobileViewEntity) {
        updateConfirmButton()
        binding.etMobileNumber.setText(item.mobileNumber)
        binding.tvCountryCode.text = "${item.countryCode.dialCode} ${item.countryCode.code}"
    }

    private fun updateConfirmButton() {
        val isEnable = item.mobileNumber.isNotBlank()
        binding.tvConfirm.isEnabled = isEnable
        binding.tvConfirm.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

}