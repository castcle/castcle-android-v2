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

package com.castcle.android.presentation.setting.verify_otp.item_verify_otp

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.text.Html
import android.widget.TextView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemVerifyOtpBinding
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.presentation.setting.verify_otp.VerifyOtpListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber
import java.util.concurrent.*

class VerifyOtpViewHolder(
    private val binding: ItemVerifyOtpBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: VerifyOtpListener,
) : CastcleViewHolder<VerifyOtpViewEntity>(binding.root) {

    override var item = VerifyOtpViewEntity()

    init {
        compositeDisposable += binding.llResendOtp.onClick {
            listener.onResentOtpClicked(otp = item.otp)
        }
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onVerifyOtp(item.otp.copy(otp = item.otpNumber))
        }
        compositeDisposable += binding.etOtp.onTextChange {
            item.otpNumber = it
            updateConfirmButton()
        }
        compositeDisposable += Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateCountdownTimer()
            }, {
                Timber.e(it)
            })
        binding.etOtp.setOnLongClickListener {
            context().getSystemService(CLIPBOARD_SERVICE)
                ?.cast<ClipboardManager>()
                ?.primaryClip
                ?.getItemAt(0)
                ?.text
                ?.toString()
                ?.ifBlank { null }
                ?.let(binding.etOtp::setText)
            true
        }
    }

    override fun bind(bindItem: VerifyOtpViewEntity) {
        updateConfirmButton()
        updateCountdownTimer()
        binding.etOtp.setText(item.otpNumber)
        binding.ivIcon.setImageResource(
            when (item.otp.type) {
                is OtpType.Email, is OtpType.Password -> when (item.otp.objective) {
                    is OtpObjective.ForgotPassword -> R.drawable.ic_verify_otp_forgot_password
                    else -> R.drawable.ic_verify_otp_email
                }
                is OtpType.Mobile -> R.drawable.ic_verify_otp_mobile
            }
        )
        binding.tvTitle.text = when (item.otp.type) {
            is OtpType.Email, is OtpType.Password -> when (item.otp.objective) {
                is OtpObjective.ForgotPassword -> string(R.string.fragment_verify_otp_title_8)
                else -> string(R.string.fragment_verify_otp_title_6)
            }
            is OtpType.Mobile -> string(R.string.fragment_verify_otp_title_4)
        }
        binding.tvDescription.text = when (item.otp.type) {
            is OtpType.Email, is OtpType.Password -> when (item.otp.objective) {
                is OtpObjective.ForgotPassword -> context().getString(
                    R.string.fragment_verify_otp_title_9,
                    item.otp.email,
                )
                else -> context().getString(
                    R.string.fragment_verify_otp_title_7,
                    item.otp.email,
                )
            }
            is OtpType.Mobile -> context().getString(
                R.string.fragment_verify_otp_title_5,
                item.otp.mobileNumber,
            )
        }
    }

    private fun getCountdownTime(remaining: Long): String {
        val min = remaining.div(60).takeIf { it > 0 }?.run { "$this min" } ?: ""
        val second = remaining.mod(60).takeIf { it > 0 }?.run { "$this s" } ?: ""
        return "$min $second".trim()
    }

    private fun updateConfirmButton() {
        val isEnable = item.otpNumber.length >= 6
        binding.tvConfirm.isEnabled = isEnable
        binding.tvConfirm.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

    private fun updateCountdownTimer() {
        val timeDifferent = item.otp.expiresTime
            .minus(System.currentTimeMillis())
            .div(1_000)
        val text = if (timeDifferent > 0) {
            context().getString(
                R.string.fragment_verify_otp_title_3,
                getCountdownTime(timeDifferent),
            )
        } else {
            context().getString(R.string.fragment_verify_otp_title_2)
        }
        binding.llResendOtp.isEnabled = timeDifferent <= 0
        binding.tvResendOtp.setText(
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY),
            TextView.BufferType.SPANNABLE,
        )
    }

}