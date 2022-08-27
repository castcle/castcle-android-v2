package com.castcle.android.presentation.setting.setting.item_notification

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSettingNotificationBinding
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingNotificationViewHolder(
    private val binding: ItemSettingNotificationBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener
) : CastcleViewHolder<SettingNotificationViewEntity>(binding.root) {

    override var item = SettingNotificationViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onNotificationClicked()
        }
    }

    override fun bind(bindItem: SettingNotificationViewEntity) {
        binding.tvBadges.isVisible = item.item.total() > 0
        binding.tvBadges.text = item.item.total().asCount()
        binding.tvNewNotification.isVisible = item.item.total() > 0
        binding.tvNewNotification.text = context().getString(
            R.string.fragment_setting_title_1,
            item.item.total().asCount()
        )
    }

}