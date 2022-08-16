package com.castcle.android.presentation.dialog.recast.item_select_recast_user

import androidx.core.view.isInvisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSelectRecastUserBinding
import com.castcle.android.presentation.dialog.recast.RecastDialogListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SelectRecastUserViewHolder(
    private val binding: ItemSelectRecastUserBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RecastDialogListener,
) : CastcleViewHolder<SelectRecastUserViewEntity>(binding.root) {

    override var item = SelectRecastUserViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user.id)
        }
    }

    override fun bind(bindItem: SelectRecastUserViewEntity) {
        binding.ivCheck.isInvisible = !item.isSelected
        binding.tvDisplayName.text = item.user.displayName
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.root.setBackgroundColor(
            if (item.isSelected) {
                color(R.color.black_background_3)
            } else {
                color(R.color.black_background_1)
            }
        )
    }

}