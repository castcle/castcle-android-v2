package com.castcle.android.presentation.who_to_follow.item_who_to_follow

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWhoToFollowBinding
import com.castcle.android.presentation.who_to_follow.WhoToFollowListener
import io.reactivex.disposables.CompositeDisposable

class WhoToFollowViewRenderer : CastcleViewRenderer<WhoToFollowViewEntity,
    WhoToFollowViewHolder,
    WhoToFollowListener>(R.layout.item_who_to_follow) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WhoToFollowListener,
        compositeDisposable: CompositeDisposable
    ) = WhoToFollowViewHolder(
        ItemWhoToFollowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}