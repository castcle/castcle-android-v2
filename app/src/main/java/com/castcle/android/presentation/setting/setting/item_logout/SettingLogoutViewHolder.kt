package com.castcle.android.presentation.setting.setting.item_logout

import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.constants.*
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSettingLogoutBinding
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingLogoutViewHolder(
    binding: ItemSettingLogoutBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingLogoutViewEntity>(binding.root) {

    init {
        binding.version.tvVersion.text = context().getString(
            R.string.version,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
        )
        compositeDisposable += binding.tvLogout.onClick {
            listener.onLogoutClick()
        }
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
    }

    override var item = SettingLogoutViewEntity()

}