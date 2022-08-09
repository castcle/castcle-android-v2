package com.castcle.android.presentation.dialog.item_dialog_option

import com.castcle.android.core.base.recyclerview.CastcleListener

interface DialogOptionViewListener : CastcleListener {
    fun onOptionClicked(titleId: Int)
}