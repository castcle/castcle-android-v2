package com.castcle.android.presentation.profile.item_profile_page

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemProfilePageBinding
import com.castcle.android.presentation.profile.ProfileListener
import io.reactivex.disposables.CompositeDisposable

class ProfilePageViewRenderer : CastcleViewRenderer<ProfilePageViewEntity,
    ProfilePageViewHolder,
    ProfileListener>(R.layout.item_profile_page) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ProfileListener,
        compositeDisposable: CompositeDisposable
    ) = ProfilePageViewHolder(
        ItemProfilePageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}