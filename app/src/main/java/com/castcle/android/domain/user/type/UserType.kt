package com.castcle.android.domain.user.type

import android.os.Parcelable
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize

sealed class UserType(val id: String) : Parcelable {

    @Parcelize
    object Page : UserType(id = "page")

    @Parcelize
    object People : UserType(id = "people")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Page.id -> Page
            else -> People
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: UserType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}