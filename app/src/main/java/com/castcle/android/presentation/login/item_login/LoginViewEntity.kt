package com.castcle.android.presentation.login.item_login

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoginViewEntity(
    var email: String = "",
    var password: String = "",
    override val uniqueId: String = "${R.layout.item_login}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoginViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoginViewEntity>() == this
    }

    override fun viewType() = R.layout.item_login

}