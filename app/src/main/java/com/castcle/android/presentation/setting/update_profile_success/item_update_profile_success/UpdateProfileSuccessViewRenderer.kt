package com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemUpdateProfileSuccessBinding
import com.castcle.android.presentation.setting.update_profile_success.UpdateProfileSuccessListener
import io.reactivex.disposables.CompositeDisposable

class UpdateProfileSuccessViewRenderer : CastcleViewRenderer<UpdateProfileSuccessViewEntity,
    UpdateProfileSuccessViewHolder,
    UpdateProfileSuccessListener>(R.layout.item_update_profile_success) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: UpdateProfileSuccessListener,
        compositeDisposable: CompositeDisposable
    ) = UpdateProfileSuccessViewHolder(
        ItemUpdateProfileSuccessBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}