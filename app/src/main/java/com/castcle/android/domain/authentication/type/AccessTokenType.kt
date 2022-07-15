package com.castcle.android.domain.authentication.type

import androidx.room.TypeConverter

sealed class AccessTokenType(val id: String) {

    object Guest : AccessTokenType(id = "guest")

    object Member : AccessTokenType(id = "member")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Member.id -> Member
            else -> Guest
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: AccessTokenType): String = item.id

        @TypeConverter
        fun toEntity(item: String): AccessTokenType = getFromId(item)

    }

}