package com.castcle.android.domain.cast.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CAST
import com.castcle.android.core.extensions.filterNotNullOrBlank
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.core.entity.ImageEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = TABLE_CAST)
data class CastEntity(
    @ColumnInfo(name = "${TABLE_CAST}_authorId") val authorId: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_commentCount") val commentCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_commented") val commented: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_createdAt") val createdAt: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_farmCount") val farmCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_farming") val farming: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_image") val image: List<ImageEntity> = listOf(),
    @ColumnInfo(name = "${TABLE_CAST}_isOwner") val isOwner: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_likeCount") val likeCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_liked") val liked: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_linkPreview") val linkPreview: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_linkType") val linkType: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_linkUrl") val linkUrl: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_message") val message: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_quoteCount") val quoteCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_quoted") val quoted: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_recastCount") val recastCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_recasted") val recasted: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_reported") val reported: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_type") val type: CastType = CastType.Short,
    @ColumnInfo(name = "${TABLE_CAST}_updatedAt") val updatedAt: String = "",
) {

    companion object {
        fun map(ownerUserId: String?, response: CastResponse?) = map(listOf(ownerUserId), response)

        fun map(ownerUserId: List<String?>?, response: CastResponse?) = CastEntity(
            authorId = response?.authorId ?: "",
            commentCount = response?.metrics?.commentCount ?: 0,
            commented = response?.participate?.commented ?: false,
            createdAt = response?.createdAt ?: "",
            farmCount = response?.metrics?.farmCount ?: 0,
            farming = response?.participate?.farming ?: false,
            id = response?.id ?: "",
            image = response?.photo?.contents.orEmpty().mapNotNull { ImageEntity.map(it) },
            isOwner = ownerUserId.orEmpty()
                .filterNotNullOrBlank()
                .contains(response?.authorId),
            likeCount = response?.metrics?.likeCount ?: 0,
            liked = response?.participate?.liked ?: false,
            linkPreview = response?.link?.firstOrNull()?.imagePreview ?: "",
            linkType = response?.link?.firstOrNull()?.type ?: "",
            linkUrl = response?.link?.firstOrNull()?.url ?: "",
            message = response?.message ?: "",
            quoteCount = response?.metrics?.quoteCount ?: 0,
            quoted = response?.participate?.quoted ?: false,
            recastCount = response?.metrics?.recastCount ?: 0,
            recasted = response?.participate?.recasted ?: false,
            reported = response?.participate?.reported ?: false,
            type = CastType.getFromId(response?.referencedCasts?.type ?: response?.type),
            updatedAt = response?.updatedAt ?: "",
        )
    }

    class Converter {

        private val type = object : TypeToken<CastEntity>() {}.type

        @TypeConverter
        fun fromEntity(item: CastEntity?): String = Gson().toJson(item, type)

        @TypeConverter
        fun toEntity(item: String): CastEntity? = Gson().fromJson(item, type)

    }

}