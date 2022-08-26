package com.castcle.android.presentation.setting.register_mobile.item_register_mobile

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class RegisterMobileViewEntity(
    val mobileCountryCode: String = "",
    val mobileNumber: String = "",
    override val uniqueId: String = "${R.layout.item_register_mobile}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<RegisterMobileViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<RegisterMobileViewEntity>() == this
    }

    override fun viewType() = R.layout.item_register_mobile

}