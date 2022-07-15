package com.castcle.android.presentation.setting.item_profile_section

import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSettingProfileSectionBinding
import com.castcle.android.presentation.setting.SettingListener
import com.castcle.android.presentation.setting.item_add_page.SettingAddPageViewEntity
import com.castcle.android.presentation.setting.item_add_page.SettingAddPageViewRenderer
import com.castcle.android.presentation.setting.item_profile.SettingProfileViewRenderer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingProfileSectionViewHolder(
    binding: ItemSettingProfileSectionBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingProfileSectionViewEntity>(binding.root) {

    override var item = SettingProfileSectionViewEntity()

    private val adapter by lazy {
        CastcleAdapter(listener, compositeDisposable).apply {
            registerRenderer(SettingAddPageViewRenderer())
            registerRenderer(SettingProfileViewRenderer())
        }
    }

    init {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        compositeDisposable += binding.tvNewPage.onClick {
            listener.onNewPageClick()
        }
    }

    override fun bind(bindItem: SettingProfileSectionViewEntity) {
        adapter.submitList(item.items.plus(SettingAddPageViewEntity()))
    }

}