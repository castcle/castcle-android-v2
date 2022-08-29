package com.castcle.android.presentation.setting.verify_password.item_verify_password

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class VerifyPasswordViewEntity(
    var password: String = "",
    override val uniqueId: String = "${R.layout.item_verify_password}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<VerifyPasswordViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<VerifyPasswordViewEntity>() == this
    }

    override fun viewType() = R.layout.item_verify_password

}