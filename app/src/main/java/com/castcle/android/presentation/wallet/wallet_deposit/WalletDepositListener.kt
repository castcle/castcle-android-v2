package com.castcle.android.presentation.wallet.wallet_deposit

import android.graphics.Bitmap
import com.castcle.android.core.base.recyclerview.CastcleListener

interface WalletDepositListener : CastcleListener {
    fun onSaveQrCodeWallet(bitmap: Bitmap)
    fun onShareQrCodeWallet(bitmap: Bitmap)
}