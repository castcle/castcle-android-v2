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

package com.castcle.android.presentation.login.item_login

import android.util.Patterns
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.constants.*
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemLoginBinding
import com.castcle.android.presentation.login.LoginListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class LoginViewHolder(
    private val binding: ItemLoginBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: LoginListener,
) : CastcleViewHolder<LoginViewEntity>(binding.root) {

    override var item = LoginViewEntity()

    init {
        binding.version.tvVersion.text = context().getString(
            R.string.version,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
        )
        compositeDisposable += binding.version.ivFacebook.onClick {
            listener.onUrlClicked(URL_FACEBOOK)
        }
        compositeDisposable += binding.version.ivTwitter.onClick {
            listener.onUrlClicked(URL_TWITTER)
        }
        compositeDisposable += binding.version.ivMedium.onClick {
            listener.onUrlClicked(URL_MEDIUM)
        }
        compositeDisposable += binding.version.ivTelegram.onClick {
            listener.onUrlClicked(URL_TELEGRAM)
        }
        compositeDisposable += binding.version.ivGithub.onClick {
            listener.onUrlClicked(URL_GITHUB)
        }
        compositeDisposable += binding.version.ivDiscord.onClick {
            listener.onUrlClicked(URL_DISCORD)
        }
        compositeDisposable += binding.version.tvJoinUs.onClick {
            listener.onUrlClicked(URL_JOIN_US)
        }
        compositeDisposable += binding.version.tvDocs.onClick {
            listener.onUrlClicked(URL_DOCS)
        }
        compositeDisposable += binding.version.tvWhitepaper.onClick {
            listener.onUrlClicked(URL_WHITE_PAPER)
        }
        compositeDisposable += binding.version.tvTerms.onClick {
            listener.onUrlClicked(URL_TERMS_OF_SERVICE)
        }
        compositeDisposable += binding.version.tvPrivacy.onClick {
            listener.onUrlClicked(URL_PRIVACY_POLICY)
        }
        compositeDisposable += binding.tvForgotPassword.onClick {
            listener.onForgotPasswordClicked()
        }
        compositeDisposable += binding.tvSignUp.onClick {
            listener.onSignUpClicked()
        }
        compositeDisposable += binding.btLogin.onClick {
            listener.onEmailLoginClicked(item.email, item.password)
        }
        compositeDisposable += binding.viewTwitter.onClick {
            listener.onTwitterLoginClicked()
        }
        compositeDisposable += binding.viewFacebook.onClick {
            listener.onFacebookLoginClicked()
        }
        compositeDisposable += binding.viewGoogle.onClick {
            listener.onGoogleLoginClicked()
        }
        compositeDisposable += binding.etEmail.onTextChange {
            item.email = it.trim()
            updateLoginButton()
        }
        compositeDisposable += binding.etPassword.onTextChange {
            item.password = it.trim()
            updateLoginButton()
        }
    }

    override fun bind(bindItem: LoginViewEntity) {
        updateLoginButton()
        binding.etEmail.setText(item.email)
        binding.etPassword.setText(item.password)
    }

    private fun updateLoginButton() {
        val enable = item.password.length >= 6 &&
            Patterns.EMAIL_ADDRESS.matcher(item.email).matches()
        with(binding.btLogin) {
            backgroundTintList = colorStateList(if (enable) R.color.blue else R.color.gray_1)
            isEnabled = enable
        }
    }

}