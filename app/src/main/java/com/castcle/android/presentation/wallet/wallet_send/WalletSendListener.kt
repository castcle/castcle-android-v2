package com.castcle.android.presentation.wallet.wallet_send

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType

interface WalletSendListener : CastcleListener {
    fun onAddShortcutClicked()
    fun onScanQrCodeClicked(requestType: WalletScanQrCodeRequestType)
    fun onUpdateSendButton(amount: Double, enabled: Boolean)
}