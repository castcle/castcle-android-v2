package com.castcle.android.domain.cast.type

import androidx.room.TypeConverter

sealed class CastType(val id: String) {

    object AdsContent : CastType(id = "ads-content")
    object AdsPage : CastType(id = "ads-page")
    object Image : CastType(id = "image")
    object Long : CastType(id = "long")
    object Quote : CastType(id = "quoted")
    object Recast : CastType(id = "recasted")
    object Short : CastType(id = "short")

    companion object {
        fun getFromId(id: String?) = when (id) {
            AdsPage.id -> AdsPage
            Image.id -> Image
            Long.id -> Long
            Quote.id -> Quote
            Recast.id -> Recast
            else -> Short
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: CastType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}