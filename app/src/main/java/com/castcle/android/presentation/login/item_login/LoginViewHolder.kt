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

        }
        compositeDisposable += binding.tvSignUp.onClick {

        }
        compositeDisposable += binding.btLogin.onClick {
            listener.onEmailLoginClicked(item.email, item.password)
        }
        compositeDisposable += binding.ivTwitter.onClick {
            listener.onTwitterLoginClicked()
        }
        compositeDisposable += binding.ivFacebook.onClick {
            listener.onFacebookLoginClicked()
        }
        compositeDisposable += binding.ivGoogle.onClick {
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