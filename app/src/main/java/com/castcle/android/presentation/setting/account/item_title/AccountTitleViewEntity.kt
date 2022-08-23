package com.castcle.android.presentation.setting.account.item_title

import androidx.annotation.StringRes
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class AccountTitleViewEntity(
    @StringRes val titleId: Int = R.string.fragment_account_title_1,
    override val uniqueId: String = "$titleId"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<AccountTitleViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<AccountTitleViewEntity>() == this
    }

    override fun viewType() = R.layout.item_account_title

}