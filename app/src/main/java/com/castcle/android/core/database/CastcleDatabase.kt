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

package com.castcle.android.core.database

import androidx.room.*
import com.castcle.android.core.constants.DATABASE_VERSION
import com.castcle.android.core.database.type_converters.StringListConverter
import com.castcle.android.data.ads.dao.*
import com.castcle.android.data.authentication.dao.AccessTokenDao
import com.castcle.android.data.authentication.dao.RecursiveRefreshTokenDao
import com.castcle.android.data.cast.dao.CastDao
import com.castcle.android.data.content.dao.CommentDao
import com.castcle.android.data.content.dao.ContentDao
import com.castcle.android.data.core.dao.LoadKeyDao
import com.castcle.android.data.feed.dao.FeedDao
import com.castcle.android.data.metadata.dao.CountryCodeDao
import com.castcle.android.data.notification.dao.NotificationBadgesDao
import com.castcle.android.data.search.dao.*
import com.castcle.android.data.user.dao.*
import com.castcle.android.data.wallet.dao.*
import com.castcle.android.domain.ads.entity.*
import com.castcle.android.domain.ads.type.*
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
import com.castcle.android.domain.metadata.entity.CountryCodeEntity
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity
import com.castcle.android.domain.search.entity.*
import com.castcle.android.domain.user.entity.*
import com.castcle.android.domain.user.type.*
import com.castcle.android.domain.wallet.entity.*
import com.castcle.android.domain.wallet.type.*

@Database(
    entities = [
        AccessTokenEntity::class,
        CastEntity::class,
        CommentEntity::class,
        ContentEntity::class,
        CountryCodeEntity::class,
        FeedEntity::class,
        FollowingFollowersEntity::class,
        LinkSocialEntity::class,
        LoadKeyEntity::class,
        NotificationBadgesEntity::class,
        ProfileEntity::class,
        RecentSearchEntity::class,
        RecursiveRefreshTokenEntity::class,
        SearchEntity::class,
        SearchKeywordEntity::class,
        SyncSocialEntity::class,
        UserEntity::class,
        WalletBalanceEntity::class,
        WalletDashboardEntity::class,
        WalletHistoryEntity::class,
        WalletShortcutEntity::class,
        WhoToFollowEntity::class,
        AdvertiseListEntity::class,
        AdvertiseEntity::class,
        BoostAdsEntity::class
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
    StringListConverter::class,
    UserType.Converter::class,
    WalletDashboardType.Converter::class,
    WalletHistoryFilter.Converter::class,
    WalletHistoryStatus.Converter::class,
    WalletHistoryType.Converter::class,
    ObjectiveType.Converter::class,
    PaymentType.Converter::class,
    DailyBidType.Converter::class,
    AdvertiseType.Converter::class,
    AdBoostStatusType.Converter::class,
    AdStatusType.Converter::class,
)
abstract class CastcleDatabase : RoomDatabase() {
    abstract fun accessToken(): AccessTokenDao
    abstract fun cast(): CastDao
    abstract fun comment(): CommentDao
    abstract fun content(): ContentDao
    abstract fun countryCode(): CountryCodeDao
    abstract fun feed(): FeedDao
    abstract fun followingFollowers(): FollowingFollowersDao
    abstract fun linkSocial(): LinkSocialDao
    abstract fun loadKey(): LoadKeyDao
    abstract fun notificationBadges(): NotificationBadgesDao
    abstract fun profile(): ProfileDao
    abstract fun recentSearch(): RecentSearchDao
    abstract fun recursiveRefreshToken(): RecursiveRefreshTokenDao
    abstract fun search(): SearchDao
    abstract fun searchKeyword(): SearchKeywordDao
    abstract fun syncSocial(): SyncSocialDao
    abstract fun user(): UserDao
    abstract fun walletBalance(): WalletBalanceDao
    abstract fun walletDashboard(): WalletDashboardDao
    abstract fun walletHistory(): WalletHistoryDao
    abstract fun walletShortcut(): WalletShortcutDao
    abstract fun whoToFollow(): WhoToFollowDao
    abstract fun boostAds(): BoostAdsDao
    abstract fun advertise(): AdvertiseDao
    abstract fun advertiseList(): AdvertiseListDao
}