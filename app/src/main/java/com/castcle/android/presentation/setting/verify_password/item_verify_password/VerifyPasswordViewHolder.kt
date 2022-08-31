package com.castcle.android.presentation.setting.verify_password.item_verify_password

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemVerifyPasswordBinding
import com.castcle.android.presentation.setting.verify_password.VerifyPasswordListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class VerifyPasswordViewHolder(
    private val binding: ItemVerifyPasswordBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: VerifyPasswordListener,
) : CastcleViewHolder<VerifyPasswordViewEntity>(binding.root) {

    override var item = VerifyPasswordViewEntity()

    init {
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onVerifyPasswordClicked(item.password)
        }
        compositeDisposable += binding.etPassword.onTextChange {
            item.password = it
            updateConfirmButton()
        }
    }

    override fun bind(bindItem: VerifyPasswordViewEntity) {
        updateConfirmButton()
        binding.etPassword.setText(item.password)
    }

    private fun updateConfirmButton() {
        val isEnable = item.password.isNotBlank()
        binding.tvConfirm.isEnabled = isEnable
        binding.tvConfirm.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

}