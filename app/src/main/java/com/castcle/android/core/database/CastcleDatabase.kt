package com.castcle.android.core.database

import androidx.room.*
import com.castcle.android.core.constants.DATABASE_VERSION
import com.castcle.android.data.authentication.dao.AccessTokenDao
import com.castcle.android.data.authentication.dao.RecursiveRefreshTokenDao
import com.castcle.android.data.cast.dao.CastDao
import com.castcle.android.data.content.dao.CommentDao
import com.castcle.android.data.content.dao.ContentDao
import com.castcle.android.data.core.dao.LoadKeyDao
import com.castcle.android.data.feed.dao.FeedDao
import com.castcle.android.data.notification.dao.NotificationBadgesDao
import com.castcle.android.data.search.dao.*
import com.castcle.android.data.user.dao.*
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.authentication.entity.RecursiveRefreshTokenEntity
import com.castcle.android.domain.authentication.type.AccessTokenType
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.feed.entity.FeedEntity
import com.castcle.android.domain.feed.type.FeedType
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity
import com.castcle.android.domain.search.entity.*
import com.castcle.android.domain.user.entity.*
import com.castcle.android.domain.user.type.*

@Database(
//    autoMigrations = [
//        AutoMigration(
//            from = DATABASE_VERSION.minus(1),
//            to = DATABASE_VERSION,
//        )
//    ],
    entities = [
        AccessTokenEntity::class,
        CastEntity::class,
        CommentEntity::class,
        ContentEntity::class,
        FeedEntity::class,
        FollowingFollowersEntity::class,
        LoadKeyEntity::class,
        NotificationBadgesEntity::class,
        ProfileEntity::class,
        RecentSearchEntity::class,
        RecursiveRefreshTokenEntity::class,
        SearchEntity::class,
        SearchKeywordEntity::class,
        SyncSocialEntity::class,
        UserEntity::class,
        WhoToFollowEntity::class,
    ],
    version = DATABASE_VERSION,
)
@TypeConverters(
    AccessTokenType.Converter::class,
    CastType.Converter::class,
    ContentType.Converter::class,
    FeedType.Converter::class,
    ImageEntity.Converter::class,
    ImageEntity.ListConverter::class,
    LoadKeyType.Converter::class,
    ProfileType.Converter::class,
    SocialType.Converter::class,
    UserType.Converter::class,
)
abstract class CastcleDatabase : RoomDatabase() {
    abstract fun accessToken(): AccessTokenDao
    abstract fun cast(): CastDao
    abstract fun comment(): CommentDao
    abstract fun content(): ContentDao
    abstract fun feed(): FeedDao
    abstract fun followingFollowers(): FollowingFollowersDao
    abstract fun loadKey(): LoadKeyDao
    abstract fun notificationBadges(): NotificationBadgesDao
    abstract fun profile(): ProfileDao
    abstract fun recentSearch(): RecentSearchDao
    abstract fun recursiveRefreshToken(): RecursiveRefreshTokenDao
    abstract fun search(): SearchDao
    abstract fun searchKeyword(): SearchKeywordDao
    abstract fun syncSocial(): SyncSocialDao
    abstract fun user(): UserDao
    abstract fun whoToFollow(): WhoToFollowDao
}