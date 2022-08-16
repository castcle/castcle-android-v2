package com.castcle.android.presentation.dialog.recast

import com.castcle.android.core.base.recyclerview.CastcleListener

interface RecastDialogListener : CastcleListener {
    fun onSelectUserClicked()
    fun onUserClicked(userId: String)
}