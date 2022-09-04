package com.castcle.android.presentation.wallet.wallet_scan_qr_code

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class WalletScanQrCodeRequestType(open val userId: String) : Parcelable {

    @Parcelize
    data class FromAddress(override val userId: String) : WalletScanQrCodeRequestType(userId)

    @Parcelize
    data class FromDashboard(override val userId: String) : WalletScanQrCodeRequestType(userId)

    @Parcelize
    data class FromMemo(override val userId: String) : WalletScanQrCodeRequestType(userId)

}