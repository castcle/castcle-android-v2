package com.castcle.android.domain.user.entity

import android.os.Parcelable
import androidx.room.*
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.core.extensions.filterNotNullOrBlank
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.type.UserType
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_USER)
@Parcelize
data class UserEntity(
    @ColumnInfo(name = "${TABLE_USER}_avatar") val avatar: ImageEntity = ImageEntity(),
    @ColumnInfo(name = "${TABLE_USER}_blocked") val blocked: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_blocking") val blocking: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_canUpdateCastcleId") val canUpdateCastcleId: Boolean? = null,
    @ColumnInfo(name = "${TABLE_USER}_castcleId") val castcleId: String = "",
    @ColumnInfo(name = "${TABLE_USER}_casts") val casts: Int? = null,
    @ColumnInfo(name = "${TABLE_USER}_cover") val cover: ImageEntity? = null,
    @ColumnInfo(name = "${TABLE_USER}_displayName") val displayName: String = "",
    @ColumnInfo(name = "${TABLE_USER}_dob") val dob: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_email") val email: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_followed") val followed: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_followers") val followers: Int? = null,
    @ColumnInfo(name = "${TABLE_USER}_following") val following: Int? = null,
    @ColumnInfo(name = "${TABLE_USER}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_USER}_isOwner") val isOwner: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_linkFacebook") val linkFacebook: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_linkMedium") val linkMedium: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_linkTwitter") val linkTwitter: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_linkWebsite") val linkWebsite: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_linkYoutube") val linkYoutube: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_mobileCountryCode") val mobileCountryCode: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_mobileNumber") val mobileNumber: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_overview") val overview: String? = null,
    @ColumnInfo(name = "${TABLE_USER}_passwordNotSet") val passwordNotSet: Boolean? = null,
    @ColumnInfo(name = "${TABLE_USER}_pdpa") val pdpa: Boolean? = null,
    @ColumnInfo(name = "${TABLE_USER}_type") val type: UserType = UserType.People,
    @ColumnInfo(name = "${TABLE_USER}_verifiedEmail") val verifiedEmail: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_verifiedMobile") val verifiedMobile: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_verifiedOfficial") val verifiedOfficial: Boolean = false,
    @ColumnInfo(name = "${TABLE_USER}_verifiedSocial") val verifiedSocial: Boolean = false,
) : Parcelable {

    companion object {
        fun mapOwner(response: UserResponse?) = map(listOf(), response).copy(isOwner = true)

        fun mapOwner(response: List<UserResponse>?): List<UserEntity> {
            return response.orEmpty().map { map(listOf(), it).copy(isOwner = true) }
        }

        fun map(ownerUserId: List<String?>?, response: List<UserResponse>?): List<UserEntity> {
            return response.orEmpty().map { map(ownerUserId, it) }
        }

        fun map(ownerUserId: String?, response: UserResponse?) = map(listOf(ownerUserId), response)

        fun map(ownerUserId: List<String?>?, response: UserResponse?) = UserEntity(
            avatar = ImageEntity.map(response?.images?.avatar ?: response?.avatar) ?: ImageEntity(),
            blocked = response?.blocked ?: false,
            blocking = response?.blocking ?: false,
            canUpdateCastcleId = response?.canUpdateCastcleId,
            castcleId = response?.castcleId ?: "",
            casts = response?.casts,
            cover = ImageEntity.map(response?.images?.cover),
            displayName = response?.displayName ?: "",
            dob = response?.dob,
            email = response?.email,
            followed = response?.followed ?: false,
            followers = response?.followers?.count,
            following = response?.following?.count,
            id = response?.id ?: "",
            isOwner = ownerUserId.orEmpty()
                .filterNotNullOrBlank()
                .contains(response?.id),
            linkFacebook = response?.links?.facebook,
            linkMedium = response?.links?.medium,
            linkTwitter = response?.links?.twitter,
            linkWebsite = response?.links?.website,
            linkYoutube = response?.links?.youtube,
            mobileCountryCode = response?.mobile?.countryCode,
            mobileNumber = response?.mobile?.number,
            overview = response?.overview,
            passwordNotSet = response?.passwordNotSet,
            pdpa = response?.passwordNotSet,
            type = UserType.getFromId(response?.type),
            verifiedEmail = response?.verified?.email ?: false,
            verifiedMobile = response?.verified?.mobile ?: false,
            verifiedOfficial = response?.verified?.official ?: false,
            verifiedSocial = response?.verified?.social ?: false,
        )
    }

}