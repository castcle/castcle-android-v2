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

package com.castcle.android.presentation.setting.setting

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.constants.URL_ABOUT_US
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity
import com.castcle.android.domain.setting.entity.ConfigEntity
import com.castcle.android.domain.user.entity.UserWithSyncSocialEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.setting.item_logout.SettingLogoutViewEntity
import com.castcle.android.presentation.setting.setting.item_menu.SettingMenuViewEntity
import com.castcle.android.presentation.setting.setting.item_notification.SettingNotificationViewEntity
import com.castcle.android.presentation.setting.setting.item_profile.SettingProfileViewEntity
import com.castcle.android.presentation.setting.setting.item_profile_section.SettingProfileSectionViewEntity
import com.castcle.android.presentation.setting.setting.item_verify_email.SettingVerifyEmailViewEntity
import org.koin.core.annotation.Factory

@Factory
class SettingMapper {

    fun map(
        response: Pair<List<UserWithSyncSocialEntity>, ConfigEntity?>,
        notification: List<NotificationBadgesEntity>
    ): List<CastcleViewEntity> {
        val user = response.first
        val config = response.second ?: ConfigEntity()
        val pageProfile = user.filter { it.user.type is UserType.Page }
        val userProfile = user.firstOrNull { it.user.type is UserType.People }
            ?: UserWithSyncSocialEntity()
        val notificationItems = notification.map { map ->
            SettingNotificationViewEntity(item = map)
        }.ifEmpty {
            listOf(SettingNotificationViewEntity())
        }
        val verifyEmailItems = if (
            !userProfile.user.email.isNullOrBlank() && userProfile.user.verifiedEmail == false
        ) {
            listOf(SettingVerifyEmailViewEntity())
        } else {
            listOf()
        }
        val userItems = SettingProfileViewEntity(
            userWithSocial = userProfile,
            uniqueId = userProfile.user.id
        )
        val pageItems = pageProfile.map {
            SettingProfileViewEntity(
                userWithSocial = it,
                uniqueId = it.user.id,
            )
        }
        val profileItems = SettingProfileSectionViewEntity(
            items = listOf(userItems).plus(pageItems)
        )
        val menuItems = listOf(
            SettingMenuViewEntity(
                action = { it.onAccountClick() },
                menu = R.string.account,
                menuIcon = R.drawable.ic_user,
                uniqueId = "${R.string.account}",
            ),
            SettingMenuViewEntity(
                action = { it.onAdManagerClick() },
                menu = R.string.ad_manager,
                menuIcon = R.drawable.ic_ad,
                uniqueId = "${R.string.ad_manager}",
            ),
            SettingMenuViewEntity(
                action = { it.onContentFarmingClick() },
                menu = R.string.content_farming,
                menuIcon = R.drawable.ic_content_farming,
                uniqueId = "${R.string.content_farming}",
            ),
            SettingMenuViewEntity(
                action = { it.onUrlClicked(URL_ABOUT_US) },
                menu = R.string.about_us,
                menuIcon = R.drawable.ic_info,
                uniqueId = "${R.string.about_us}",
            ),
        ).filter {
            when (it.menu) {
                R.string.ad_manager -> config.adsEnable
                R.string.content_farming -> config.farmingEnable
                else -> true
            }
        }
        return notificationItems.plus(verifyEmailItems)
            .plus(profileItems)
            .plus(menuItems)
            .plus(SettingLogoutViewEntity())
    }

}