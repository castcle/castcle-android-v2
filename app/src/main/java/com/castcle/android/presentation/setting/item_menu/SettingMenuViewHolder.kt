package com.castcle.android.presentation.setting.item_menu

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemSettingMenuBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingMenuViewHolder(
    private val binding: ItemSettingMenuBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingMenuViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            item.action(listener)
        }
    }

    override var item = SettingMenuViewEntity()

    override fun bind(bindItem: SettingMenuViewEntity) {
        binding.ivIcon.setImageResource(item.menuIcon)
        binding.tvTitle.text = string(item.menu)
    }

}