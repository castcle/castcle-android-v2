package com.castcle.android.domain.core.entity

import android.os.Parcelable
import androidx.room.TypeConverter
import com.castcle.android.core.base.response.PhotoResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageEntity(
    val original: String = "",
    val thumbnail: String = "",
) : Parcelable {

    companion object {
        fun map(url: String?): ImageEntity? {
            return if (url.isNullOrBlank()) {
                null
            } else {
                ImageEntity(original = url, thumbnail = url)
            }
        }

        fun map(response: PhotoResponse?): ImageEntity? {
            return if (response?.original.isNullOrBlank() && response?.thumbnail.isNullOrBlank()) {
                null
            } else {
                ImageEntity(
                    original = response?.original ?: response?.thumbnail ?: "",
                    thumbnail = response?.thumbnail ?: response?.original ?: "",
                )
            }
        }
    }

    class Converter {

        private val type = object : TypeToken<ImageEntity>() {}.type

        @TypeConverter
        fun fromEntity(item: ImageEntity?): String? = item?.let { Gson().toJson(it, type) }

        @TypeConverter
        fun toEntity(item: String?): ImageEntity? = item?.let { Gson().fromJson(item, type) }

    }

    class ListConverter {

        private val type = object : TypeToken<List<ImageEntity>?>() {}.type

        @TypeConverter
        fun fromEntity(item: List<ImageEntity>?): String = Gson().toJson(item, type)

        @TypeConverter
        fun toEntity(item: String): List<ImageEntity>? = Gson().fromJson(item, type)

    }

}