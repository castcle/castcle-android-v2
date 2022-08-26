package com.castcle.android.presentation.setting.register_mobile.item_register_mobile

import android.annotation.SuppressLint
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemRegisterMobileBinding
import com.castcle.android.presentation.setting.register_mobile.RegisterMobileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class RegisterMobileViewHolder(
    private val binding: ItemRegisterMobileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RegisterMobileListener
) : CastcleViewHolder<RegisterMobileViewEntity>(binding.root) {

    override var item = RegisterMobileViewEntity()

    init {
        compositeDisposable += binding.tvCountryCode.onClick {
            listener.onMobileCountryCodeClicked()
        }
        compositeDisposable += binding.etMobileNumber.onTextChange {
            item.mobileNumber = it
            updateConfirmButton()
        }
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onConfirmClicked(
                countryCode = item.countryCode,
                mobileNumber = item.mobileNumber,
            )
        }
    }

    override fun bind(bindItem: RegisterMobileViewEntity) {
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