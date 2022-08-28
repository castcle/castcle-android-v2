package com.castcle.android.domain.authentication.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class OtpType(val id: String) : Parcelable {

    @Parcelize
    object Email : OtpType(id = "email")

    @Parcelize
    object Mobile : OtpType(id = "mobile")

}