package com.castcle.android.presentation.content.item_comment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemCommentBinding
import com.castcle.android.presentation.content.ContentListener
import io.reactivex.disposables.CompositeDisposable

class CommentViewRenderer : CastcleViewRenderer<CommentViewEntity,
    CommentViewHolder,
    ContentListener>(R.layout.item_comment) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ContentListener,
        compositeDisposable: CompositeDisposable
    ) = CommentViewHolder(
        ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}