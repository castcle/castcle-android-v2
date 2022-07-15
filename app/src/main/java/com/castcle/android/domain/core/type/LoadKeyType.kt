package com.castcle.android.domain.core.type

import androidx.room.TypeConverter

sealed class LoadKeyType(val id: String) {

    object Feed : LoadKeyType(id = "feed")

    object Profile : LoadKeyType(id = "profile")

    object WhoToFollow : LoadKeyType(id = "whoToFollow")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Profile.id -> Profile
            WhoToFollow.id -> WhoToFollow
            else -> Feed
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: LoadKeyType): String = item.id

        @TypeConverter
        fun toEntity(item: String): LoadKeyType = getFromId(item)

    }

}