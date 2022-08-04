package com.castcle.android.presentation.content.item_reply

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemReplyBinding
import com.castcle.android.presentation.content.ContentListener
import io.reactivex.disposables.CompositeDisposable

class ReplyViewRenderer : CastcleViewRenderer<ReplyViewEntity,
    ReplyViewHolder,
    ContentListener>(R.layout.item_reply) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ContentListener,
        compositeDisposable: CompositeDisposable
    ) = ReplyViewHolder(
        ItemReplyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}