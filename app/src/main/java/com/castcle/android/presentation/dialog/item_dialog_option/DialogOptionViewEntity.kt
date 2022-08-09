package com.castcle.android.presentation.dialog.item_dialog_option

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class DialogOptionViewEntity(
    @DrawableRes val icon: Int = R.drawable.ic_recast,
    @StringRes val title: Int = R.string.recast,
    override val uniqueId: String = "$title"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<DialogOptionViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<DialogOptionViewEntity>() == this
    }

    override fun viewType() = R.layout.item_dialog_option

}