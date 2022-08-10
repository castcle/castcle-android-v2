package com.castcle.android.data.user.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.entity.UserWithSyncSocialEntity
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

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1")
    suspend fun get(): List<UserEntity>

    @Query("SELECT * FROM $TABLE_USER WHERE user_type = :type AND user_isOwner = 1")
    suspend fun get(type: UserType): List<UserEntity>

    @Query("SELECT * FROM $TABLE_USER WHERE user_id = :userId")
    suspend fun get(userId: String): List<UserEntity>

    @Query("UPDATE $TABLE_USER SET user_casts = case when user_casts IS NOT NULL then user_casts + 1 else NULL end  WHERE user_id = :userId")
    suspend fun increaseCastCount(userId: String)

    @Query("UPDATE $TABLE_USER SET user_followers = case when user_followers IS NOT NULL then user_followers + 1 else NULL end  WHERE user_id = :userId")
    suspend fun increaseFollowers(userId: String)

    @Query("UPDATE $TABLE_USER SET user_following = case when user_following IS NOT NULL then user_following + 1 else NULL end  WHERE user_id = :userId")
    suspend fun increaseFollowing(userId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<UserEntity>): List<Long>

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1")
    @Transaction
    fun retrieveWithSyncSocial(): Flow<List<UserWithSyncSocialEntity>>

    @Query("SELECT * FROM $TABLE_USER WHERE user_isOwner = 1 AND user_type = :type")
    fun retrieve(type: UserType): Flow<List<UserEntity>>

    @Query(
        "UPDATE $TABLE_USER SET user_avatar = :avatar, " +
            "user_blocked = :blocked, " +
            "user_blocking = :blocking, " +
            "user_canUpdateCastcleId = case when :canUpdateCastcleId IS NOT NULL then :canUpdateCastcleId else user_canUpdateCastcleId end, " +
            "user_castcleId = :castcleId, " +
            "user_casts = case when :casts IS NOT NULL then :casts else user_casts end, " +
            "user_cover = case when :cover IS NOT NULL then :cover else user_cover end, " +
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
            "user_verifiedEmail = :verifiedEmail, " +
            "user_verifiedMobile = :verifiedMobile, " +
            "user_verifiedOfficial = :verifiedOfficial, " +
            "user_verifiedSocial = :verifiedSocial " +
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
        verifiedEmail: Boolean,
        verifiedMobile: Boolean,
        verifiedOfficial: Boolean,
        verifiedSocial: Boolean,
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