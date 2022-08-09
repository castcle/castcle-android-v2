package com.castcle.android.presentation.dialog.option

import com.castcle.android.core.base.recyclerview.CastcleListener

interface OptionDialogListener : CastcleListener {
    fun onOptionClicked(titleId: Int)
}