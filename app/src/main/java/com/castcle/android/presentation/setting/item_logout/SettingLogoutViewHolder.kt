package com.castcle.android.presentation.setting.item_logout

import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.constants.*
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSettingLogoutBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingLogoutViewHolder(
    binding: ItemSettingLogoutBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingLogoutViewEntity>(binding.root) {

    init {
        binding.tvVersion.text = context().getString(
            R.string.version,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
        )
        compositeDisposable += binding.tvLogout.onClick {
            listener.onLogoutClick()
        }
        compositeDisposable += binding.ivFacebook.onClick {
            listener.onUrlClicked(URL_FACEBOOK)
        }
        compositeDisposable += binding.ivTwitter.onClick {
            listener.onUrlClicked(URL_TWITTER)
        }
        compositeDisposable += binding.ivMedium.onClick {
            listener.onUrlClicked(URL_MEDIUM)
        }
        compositeDisposable += binding.ivTelegram.onClick {
            listener.onUrlClicked(URL_TELEGRAM)
        }
        compositeDisposable += binding.ivGithub.onClick {
            listener.onUrlClicked(URL_GITHUB)
        }
        compositeDisposable += binding.ivDiscord.onClick {
            listener.onUrlClicked(URL_DISCORD)
        }
        compositeDisposable += binding.tvJoinUs.onClick {
            listener.onUrlClicked(URL_JOIN_US)
        }
        compositeDisposable += binding.tvDocs.onClick {
            listener.onUrlClicked(URL_DOCS)
        }
        compositeDisposable += binding.tvWhitepaper.onClick {
            listener.onUrlClicked(URL_WHITE_PAPER)
        }
        compositeDisposable += binding.tvTerms.onClick {
            listener.onUrlClicked(URL_TERMS_OF_SERVICE)
        }
        compositeDisposable += binding.tvPrivacy.onClick {
            listener.onUrlClicked(URL_PRIVACY_POLICY)
        }
    }

    override var item = SettingLogoutViewEntity()

}