package com.castcle.android.domain.authentication.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class OtpObjective(val id: String) : Parcelable {

    @Parcelize
    object ChangePassword : OtpObjective(id = "change_password")

    @Parcelize
    object VerifyMobile : OtpObjective(id = "verify_mobile")

}