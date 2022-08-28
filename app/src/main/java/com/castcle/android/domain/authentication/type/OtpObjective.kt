package com.castcle.android.domain.authentication.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class OtpObjective(val id: String) : Parcelable {

    @Parcelize
    object VerifyMobile : OtpObjective(id = "verify_mobile")

    companion object {
        fun getFromId(id: String?) = when (id) {
            VerifyMobile.id -> VerifyMobile
            else -> VerifyMobile
        }
    }

}