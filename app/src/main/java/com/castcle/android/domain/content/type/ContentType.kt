package com.castcle.android.domain.content.type

import androidx.room.TypeConverter

sealed class ContentType(val id: String) {

    object Comment : ContentType("comment")

    object Content : ContentType("content")

    object Metrics : ContentType("metrics")

    object Reply : ContentType("reply")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Comment.id -> Comment
            Content.id -> Content
            Metrics.id -> Metrics
            else -> Reply
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: ContentType): String = item.id

        @TypeConverter
        fun toEntity(item: String): ContentType = getFromId(item)

    }

}