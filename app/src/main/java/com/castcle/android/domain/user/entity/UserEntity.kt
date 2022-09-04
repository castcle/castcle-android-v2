/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.domain.user.entity

import android.os.Parcelable
import androidx.room.*
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.core.extensions.filterNotNullOrBlank
import com.castcle.android.core.extensions.toMilliSecond
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
    @ColumnInfo(name = "${TABLE_USER}_createdAt") val createdAt: Long? = null,
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

    fun isNotVerified(): Boolean {
        return !verifiedEmail && !verifiedMobile && !verifiedOfficial && !verifiedSocial
    }

    companion object {
        fun mapOwner(response: UserResponse?) = map(listOf(), response).copy(isOwner = true)

        fun mapOwner(response: List<UserResponse>?): List<UserEntity> {
            return response.orEmpty().map { map(listOf(), it).copy(isOwner = true) }
        }

        fun map(
            ownerUserId: List<String?>?,
            response: List<UserResponse>?
        ): MutableList<UserEntity> {
            return response.orEmpty().map { map(ownerUserId, it) }.toMutableList()
        }

        fun map(ownerUserId: List<String?>?, response: UserResponse?) = UserEntity(
            avatar = ImageEntity.map(response?.images?.avatar ?: response?.avatar) ?: ImageEntity(),
            blocked = response?.blocked ?: false,
            blocking = response?.blocking ?: false,
            canUpdateCastcleId = response?.canUpdateCastcleId,
            castcleId = response?.castcleId.orEmpty(),
            casts = response?.casts,
            cover = ImageEntity.map(response?.images?.cover),
            createdAt = response?.createdAt?.toMilliSecond(),
            displayName = response?.displayName.orEmpty(),
            dob = response?.dob,
            email = response?.email,
            followed = response?.followed ?: false,
            followers = response?.followers?.count,
            following = response?.following?.count,
            id = response?.id.orEmpty(),
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