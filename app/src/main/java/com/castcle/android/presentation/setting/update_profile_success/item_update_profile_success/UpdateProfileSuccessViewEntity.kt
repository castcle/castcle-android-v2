package com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success

import androidx.annotation.DrawableRes
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class UpdateProfileSuccessViewEntity(
    val description: String = "",
    @DrawableRes val icon: Int = R.drawable.ic_update_profile_success,
    val title: String = "",
    override val uniqueId: String = "${R.layout.item_update_profile_success}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<UpdateProfileSuccessViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<UpdateProfileSuccessViewEntity>() == this
    }

    override fun viewType() = R.layout.item_update_profile_success

}