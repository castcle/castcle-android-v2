package com.castcle.android.presentation.resent_verify_email.item_resent_verify_email

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ResentVerifyEmailViewEntity(
    val email: String = "",
    override val uniqueId: String = "${R.layout.item_resent_verify_email}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ResentVerifyEmailViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ResentVerifyEmailViewEntity>() == this
    }

    override fun viewType() = R.layout.item_resent_verify_email

}