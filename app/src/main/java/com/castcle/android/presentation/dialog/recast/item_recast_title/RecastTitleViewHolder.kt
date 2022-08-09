package com.castcle.android.presentation.dialog.recast.item_recast_title

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemRecastTitleBinding
import com.castcle.android.presentation.dialog.recast.RecastDialogListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class RecastTitleViewHolder(
    private val binding: ItemRecastTitleBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RecastDialogListener,
) : CastcleViewHolder<RecastTitleViewEntity>(binding.root) {

    override var item = RecastTitleViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            if (item.user != null) {
                listener.onSelectUserClicked()
            }
        }
    }

    override fun bind(bindItem: RecastTitleViewEntity) {
        binding.tvDisplayName.text = item.user?.displayName ?: string(R.string.dialog_recast_title)
        binding.ivAvatar.loadAvatarImage(item.user?.avatar?.thumbnail)
        binding.flAvatar.isVisible = item.user != null
        binding.ivIcon.isVisible = item.user != null
    }

}