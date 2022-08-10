package com.castcle.android.presentation.dialog.option.item_option_dialog

import androidx.annotation.DrawableRes
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class OptionDialogViewEntity(
    val eventType: Int = 0,
    @DrawableRes val icon: Int = R.drawable.ic_recast,
    val title: Any = "",
    override val uniqueId: String = title.toString()
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<OptionDialogViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<OptionDialogViewEntity>() == this
    }

    override fun viewType() = R.layout.item_option_dialog

}