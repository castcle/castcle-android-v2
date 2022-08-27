package com.castcle.android.presentation.resent_verify_email.item_resent_verify_email

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemResentVerifyEmailBinding
import com.castcle.android.presentation.resent_verify_email.ResentVerifyEmailListener
import io.reactivex.disposables.CompositeDisposable

class ResentVerifyEmailViewRenderer : CastcleViewRenderer<ResentVerifyEmailViewEntity,
    ResentVerifyEmailViewHolder,
    ResentVerifyEmailListener>(R.layout.item_resent_verify_email) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ResentVerifyEmailListener,
        compositeDisposable: CompositeDisposable
    ) = ResentVerifyEmailViewHolder(
        ItemResentVerifyEmailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}