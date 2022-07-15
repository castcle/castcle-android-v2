package com.castcle.android.presentation.profile.item_profile_user

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemProfileUserBinding
import com.castcle.android.presentation.profile.ProfileListener
import io.reactivex.disposables.CompositeDisposable

class ProfileUserViewRenderer : CastcleViewRenderer<ProfileUserViewEntity,
    ProfileUserViewHolder,
    ProfileListener>(R.layout.item_profile_user) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ProfileListener,
        compositeDisposable: CompositeDisposable
    ) = ProfileUserViewHolder(
        ItemProfileUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}