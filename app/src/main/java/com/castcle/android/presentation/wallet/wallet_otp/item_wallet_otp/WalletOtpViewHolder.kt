/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.wallet.wallet_otp.item_wallet_otp

import android.widget.TextView
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletOtpBinding
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.presentation.wallet.wallet_otp.WalletOtpListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber
import java.util.concurrent.*

class WalletOtpViewHolder(
    private val binding: ItemWalletOtpBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletOtpListener,
) : CastcleViewHolder<WalletOtpViewEntity>(binding.root) {

    override var item = WalletOtpViewEntity()

    init {
        compositeDisposable += binding.tvResendEmail.onClick {
            listener.onResendOtpEmail(item.otpEmail)
        }
        compositeDisposable += binding.tvResendMobile.onClick {
            listener.onResendOtpMobile(item.otpMobile)
        }
        compositeDisposable += binding.etOtpEmail.onTextChange {
            item.otpNumberEmail = it
            updateConfirmButton()
        }
        compositeDisposable += binding.etOtpMobile.onTextChange {
            item.otpNumberMobile = it
            updateConfirmButton()
        }
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onConfirmClicked(
                otpEmail = item.otpEmail.copy(otp = binding.etOtpEmail.text.toString()),
                otpMobile = item.otpMobile.copy(otp = binding.etOtpMobile.text.toString()),
            )
        }
        compositeDisposable += Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateCountdownTimer(
                    countdownView = binding.tvCountdownEmail,
                    otp = item.otpEmail,
                    resendView = binding.tvResendEmail,
                )
                updateCountdownTimer(
                    countdownView = binding.tvCountdownMobile,
                    otp = item.otpMobile,
                    resendView = binding.tvResendMobile,
                )
            }, {
                Timber.e(it)
            })
    }

    override fun bind(bindItem: WalletOtpViewEntity) {
        updateConfirmButton()
        binding.etOtpEmail.setText(item.otpNumberEmail)
        binding.etOtpMobile.setText(item.otpNumberMobile)
        binding.tvToEmail.text = context().getString(
            R.string.fragment_wallet_otp_title_3,
            item.otpEmail.email
        )
        binding.tvToMobile.text = context().getString(
            R.string.fragment_wallet_otp_title_3,
            "(${item.otpMobile.countryCode}) ${item.otpMobile.mobileNumber}"
        )
        updateCountdownTimer(
            countdownView = binding.tvCountdownEmail,
            otp = item.otpEmail,
            resendView = binding.tvResendEmail,
        )
        updateCountdownTimer(
            countdownView = binding.tvCountdownMobile,
            otp = item.otpMobile,
            resendView = binding.tvResendMobile,
        )
    }

    private fun getCountdownTime(remaining: Long): String {
        val min = remaining.div(60).takeIf { it > 0 }?.run { "$this min" } ?: ""
        val second = remaining.mod(60).takeIf { it > 0 }?.run { "$this s" } ?: ""
        return "$min $second".trim()
    }

    private fun updateConfirmButton() {
        val isEnable = item.otpNumberEmail.length >= 6 && item.otpNumberMobile.length >= 6
        binding.tvConfirm.isEnabled = isEnable
        binding.tvConfirm.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

    private fun updateCountdownTimer(
        countdownView: TextView,
        otp: OtpEntity,
        resendView: TextView,
    ) {
        val timeDifferent = otp.expiresTime
            .minus(System.currentTimeMillis())
            .div(1_000)
        countdownView.isVisible = timeDifferent > 0
        countdownView.text = getCountdownTime(timeDifferent)
        resendView.isEnabled = timeDifferent <= 0
        resendView.setTextColor(
            if (timeDifferent > 0) {
                color(R.color.gray_1)
            } else {
                color(R.color.blue)
            }
        )
    }

}