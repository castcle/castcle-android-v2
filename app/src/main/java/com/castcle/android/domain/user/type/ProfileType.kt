package com.castcle.android.domain.user.type

import androidx.room.TypeConverter

sealed class ProfileType(val id: String) {

    object Content : ProfileType(id = "content")

    object NewCast : ProfileType(id = "new-cast")

    object Profile : ProfileType(id = "profile")

    companion object {
        fun getFromId(id: String?) = when (id) {
            NewCast.id -> NewCast
            Profile.id -> Profile
            else -> Content
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: ProfileType): String = item.id

        @TypeConverter
        fun toEntity(item: String): ProfileType = getFromId(item)

    }

}