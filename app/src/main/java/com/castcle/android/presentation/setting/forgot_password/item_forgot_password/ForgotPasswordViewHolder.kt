package com.castcle.android.presentation.setting.forgot_password.item_forgot_password

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemForgotPasswordBinding
import com.castcle.android.presentation.setting.forgot_password.ForgotPasswordListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ForgotPasswordViewHolder(
    private val binding: ItemForgotPasswordBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ForgotPasswordListener,
) : CastcleViewHolder<ForgotPasswordViewEntity>(binding.root) {

    override var item = ForgotPasswordViewEntity()

    init {
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onForgotPasswordClicked(item.email)
        }
        compositeDisposable += binding.etEmail.onTextChange {
            item.email = it
            updateConfirmButton()
        }
    }

    override fun bind(bindItem: ForgotPasswordViewEntity) {
        updateConfirmButton()
        binding.etEmail.setText(item.email)
    }

    private fun updateConfirmButton() {
        val isEnable = item.email.isNotBlank()
        binding.tvConfirm.isEnabled = isEnable
        binding.tvConfirm.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

}