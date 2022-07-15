package com.castcle.android.domain.user.type

import androidx.room.TypeConverter

sealed class SocialType(val id: String) {

    object Facebook : SocialType(id = "facebook")

    object Google : SocialType(id = "google")

    object Twitter : SocialType(id = "twitter")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Facebook.id -> Facebook
            Google.id -> Google
            else -> Twitter
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: SocialType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}