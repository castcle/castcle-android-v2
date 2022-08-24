package com.castcle.android.presentation.setting.account.item_menu

import androidx.annotation.*
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.presentation.setting.account.AccountListener

data class AccountMenuViewEntity(
    val action: (AccountListener) -> Unit = {},
    val description: Any? = null,
    val detail: Any? = null,
    @ColorRes val detailColor: Int = R.color.gray_1,
    @ColorRes val imageColor: Int = R.color.blue_facebook,
    @DrawableRes val imageIcon: Int? = null,
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