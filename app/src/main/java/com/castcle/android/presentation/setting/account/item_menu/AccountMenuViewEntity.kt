package com.castcle.android.presentation.setting.account.item_menu

import androidx.annotation.StringRes
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class AccountMenuViewEntity(
    val description: Any = "",
    val showArrow: Boolean = true,
    @StringRes val titleId: Int = R.string.email,
    override val uniqueId: String = "$titleId"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<AccountMenuViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<AccountMenuViewEntity>() == this
    }

    override fun viewType() = R.layout.item_account_menu

}