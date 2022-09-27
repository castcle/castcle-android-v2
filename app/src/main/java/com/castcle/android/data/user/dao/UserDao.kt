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

package com.castcle.android.data.user.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.*
import com.castcle.android.domain.user.type.UserType
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("UPDATE $TABLE_USER SET user_casts = case when user_casts IS NOT NULL then user_casts - 1 else NULL end  WHERE user_id = :userId")
    suspend fun decreaseCastCount(userId: String)

    @Query("UPDATE $TABLE_USER SET user_followers = case when user_followers IS NOT NULL then user_followers - 1 else NULL end  WHERE user_id = :userId")
    suspend fun decreaseFollowers(userId: String)

    @Query("UPDATE $TABLE_USER SET user_following = case when user_following IS NOT NULL then user_following - 1 else NULL end  WHERE user_id = :userId")
    suspend fun decreaseFollowing(userId: String)

    @Query("DELETE FROM $TABLE_USER")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_USER WHERE user_id = :userId")
    suspend fun delete(userId: String)

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1")
    suspend fun get(): List<UserEntity>

    @Query("SELECT * FROM $TABLE_USER WHERE user_type = :type AND user_isOwner = 1 ORDER BY user_createdAt ASC")
    suspend fun get(type: UserType): List<UserEntity>

    @Query("SELECT * FROM $TABLE_USER WHERE user_id = :userId")
    suspend fun get(userId: String): List<UserEntity>

    @Query("SELECT * FROM $TABLE_USER WHERE user_id = :userId")
    fun getByCastcleID(userId: String): Flow<UserEntity?>

    @Query("UPDATE $TABLE_USER SET user_casts = case when user_casts IS NOT NULL then user_casts + 1 else NULL end  WHERE user_id = :userId")
    suspend fun increaseCastCount(userId: String)

    @Query("UPDATE $TABLE_USER SET user_followers = case when user_followers IS NOT NULL then user_followers + 1 else NULL end  WHERE user_id = :userId")
    suspend fun increaseFollowers(userId: String)

    @Query("UPDATE $TABLE_USER SET user_following = case when user_following IS NOT NULL then user_following + 1 else NULL end  WHERE user_id = :userId")
    suspend fun increaseFollowing(userId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<UserEntity>): List<Long>

    @Query("SELECT * FROM $TABLE_USER WHERE user_id = :userId")
    fun retrieve(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1 AND user_type = :type")
    fun retrieve(type: UserType): Flow<List<UserEntity>>

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1 AND user_type = :type")
    @Transaction
    fun retrieveWithLinkSocial(type: UserType): Flow<UserWithLinkSocialEntity?>

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1 ORDER BY user_createdAt ASC")
    @Transaction
    fun retrieveWithSyncSocial(): Flow<List<UserWithSyncSocialEntity>>

    @Query("UPDATE $TABLE_USER SET user_dob = :dob  WHERE user_id = :userId")
    suspend fun updateProfileBirthDate(dob: String, userId: String)

    @Query(
        "UPDATE $TABLE_USER SET user_avatar = :avatar, " +
            "user_blocked = :blocked, " +
            "user_blocking = :blocking, " +
            "user_canUpdateCastcleId = case when :canUpdateCastcleId IS NOT NULL then :canUpdateCastcleId else user_canUpdateCastcleId end, " +
            "user_castcleId = :castcleId, " +
            "user_casts = case when :casts IS NOT NULL then :casts else user_casts end, " +
            "user_cover = case when :cover IS NOT NULL then :cover else user_cover end, " +
            "user_createdAt = case when :createdAt IS NOT NULL then :createdAt else user_createdAt end, " +
            "user_displayName = :displayName, " +
            "user_dob = case when :dob IS NOT NULL then :dob else user_dob end, " +
            "user_email = case when :email IS NOT NULL then :email else user_email end, " +
            "user_followed = :followed, " +
            "user_followers = case when :followers IS NOT NULL then :followers else user_followers end, " +
            "user_following = case when :following IS NOT NULL then :following else user_following end, " +
            "user_isOwner = :isOwner, " +
            "user_linkFacebook = case when :linkFacebook IS NOT NULL then :linkFacebook else user_linkFacebook end, " +
            "user_linkMedium = case when :linkMedium IS NOT NULL then :linkMedium else user_linkMedium end, " +
            "user_linkTwitter = case when :linkTwitter IS NOT NULL then :linkTwitter else user_linkTwitter end, " +
            "user_linkWebsite = case when :linkWebsite IS NOT NULL then :linkWebsite else user_linkWebsite end, " +
            "user_linkYoutube = case when :linkYoutube IS NOT NULL then :linkYoutube else user_linkYoutube end, " +
            "user_mobileCountryCode = case when :mobileCountryCode IS NOT NULL then :mobileCountryCode else user_mobileCountryCode end, " +
            "user_mobileNumber = case when :mobileNumber IS NOT NULL then :mobileNumber else user_mobileNumber end, " +
            "user_overview = case when :overview IS NOT NULL then :overview else user_overview end, " +
            "user_passwordNotSet = case when :passwordNotSet IS NOT NULL then :passwordNotSet else user_passwordNotSet end, " +
            "user_pdpa = case when :pdpa IS NOT NULL then :pdpa else user_pdpa end, " +
            "user_type = case when :type IS NOT NULL then :type else user_type end, " +
            "user_contactEmail = case when :contactEmail IS NOT NULL then :contactEmail else user_contactEmail end, " +
            "user_contactNumber = case when :contactNumber IS NOT NULL then :contactNumber else user_contactNumber end, " +
            "user_verifiedEmail = case when :verifiedEmail IS NOT NULL then :verifiedEmail else user_verifiedEmail end, " +
            "user_verifiedMobile = case when :verifiedMobile IS NOT NULL then :verifiedMobile else user_verifiedMobile end, " +
            "user_verifiedOfficial = case when :verifiedOfficial IS NOT NULL then :verifiedOfficial else user_verifiedOfficial end, " +
            "user_verifiedSocial = case when :verifiedSocial IS NOT NULL then :verifiedSocial else user_verifiedSocial end " +
            "WHERE user_id = :id OR user_castcleId = :castcleId"
    )
    suspend fun update(
        avatar: ImageEntity,
        blocked: Boolean,
        blocking: Boolean,
        canUpdateCastcleId: Boolean?,
        castcleId: String,
        casts: Int?,
        cover: ImageEntity?,
        createdAt: Long?,
        displayName: String,
        dob: String?,
        email: String?,
        followed: Boolean,
        followers: Int?,
        following: Int?,
        id: String,
        isOwner: Boolean,
        linkFacebook: String?,
        linkMedium: String?,
        linkTwitter: String?,
        linkWebsite: String?,
        linkYoutube: String?,
        mobileCountryCode: String?,
        mobileNumber: String?,
        overview: String?,
        passwordNotSet: Boolean?,
        pdpa: Boolean?,
        type: UserType,
        verifiedEmail: Boolean?,
        verifiedMobile: Boolean?,
        verifiedOfficial: Boolean?,
        verifiedSocial: Boolean?,
        contactEmail: String?,
        contactNumber: String?,
    )

    @Transaction
    suspend fun update(item: UserEntity) {
        update(
            avatar = item.avatar,
            blocked = item.blocked,
            blocking = item.blocking,
            canUpdateCastcleId = item.canUpdateCastcleId,
            castcleId = item.castcleId,
            casts = item.casts,
            cover = item.cover,
            createdAt = item.createdAt,
            displayName = item.displayName,
            dob = item.dob,
            email = item.email,
            followed = item.followed,
            followers = item.followers,
            following = item.following,
            id = item.id,
            isOwner = item.isOwner,
            linkFacebook = item.linkFacebook,
            linkMedium = item.linkMedium,
            linkTwitter = item.linkTwitter,
            linkWebsite = item.linkWebsite,
            linkYoutube = item.linkYoutube,
            mobileCountryCode = item.mobileCountryCode,
            mobileNumber = item.mobileNumber,
            overview = item.overview,
            passwordNotSet = item.passwordNotSet,
            pdpa = item.pdpa,
            type = item.type,
            verifiedEmail = item.verifiedEmail,
            verifiedMobile = item.verifiedMobile,
            verifiedOfficial = item.verifiedOfficial,
            verifiedSocial = item.verifiedSocial,
            contactEmail = item.contactEmail,
            contactNumber = item.contactNumber,
        )
    }

    @Transaction
    suspend fun upsert(item: UserEntity) {
        upsert(listOf(item))
    }

    @Transaction
    suspend fun upsert(items: List<UserEntity>) {
        insert(items).forEachIndexed { index, insertResult ->
            if (insertResult == -1L) {
                items.getOrNull(index)?.let { update(it) }
            }
        }
    }

}