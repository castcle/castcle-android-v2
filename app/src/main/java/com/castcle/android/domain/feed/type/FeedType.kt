package com.castcle.android.domain.feed.type

import androidx.room.TypeConverter

sealed class FeedType(val id: String) {

    object AdsContent : FeedType(id = "ads-content")

    object AdsPage : FeedType(id = "ads-page")

    object Content : FeedType(id = "content")

    object NewCast : FeedType(id = "new-cast")

    object WhoToFollow : FeedType(id = "suggestion-follow")

    companion object {
        fun getFromId(id: String?) = when (id) {
            AdsContent.id -> AdsContent
            AdsPage.id -> AdsPage
            Content.id -> Content
            WhoToFollow.id -> WhoToFollow
            else -> NewCast
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: FeedType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}