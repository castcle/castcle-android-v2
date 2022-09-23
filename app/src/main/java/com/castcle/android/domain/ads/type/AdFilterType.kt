package com.castcle.android.domain.ads.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AdFilterType(val filter: String, val filterName: String): Parcelable {
    @Parcelize
    object All : AdFilterType("all", filterName = "All")

    @Parcelize
    object Today : AdFilterType("today", filterName = "Today")

    @Parcelize
    object ThisWeek : AdFilterType("week", filterName = "This week")

    @Parcelize
    object ThisMonth : AdFilterType("month", filterName = "This month")

    @Parcelize
    object Cancel : AdFilterType("cancel", filterName = "Cancel")
}