package com.castcle.android.presentation.setting.change_password.item_change_password

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ChangePasswordViewEntity(
    var confirmPassword: String = "",
    var password: String = "",
    override val uniqueId: String = "${R.layout.item_change_password}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ChangePasswordViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ChangePasswordViewEntity>() == this
    }

    override fun viewType() = R.layout.item_change_password

}