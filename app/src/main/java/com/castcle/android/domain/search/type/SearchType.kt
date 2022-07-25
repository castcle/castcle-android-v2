package com.castcle.android.domain.search.type

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class SearchType(val id: String, val index: Int) : Parcelable {

    @Parcelize
    object Lastest : SearchType(id = "lastest", index = 2)

    @Parcelize
    object People : SearchType(id = "people", index = 4)

    @Parcelize
    object Photo : SearchType(id = "photo", index = 3)

    @Parcelize
    object Trend : SearchType(id = "trend", index = 1)

    companion object {
        fun getFromIndex(index: Int?) = when (index) {
            Lastest.index -> Lastest
            People.index -> People
            Photo.index -> Photo
            else -> Photo
        }
    }

}