package com.castcle.android.presentation.setting

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.constants.URL_ABOUT_US
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity
import com.castcle.android.domain.user.entity.UserWithSyncSocialEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.item_logout.SettingLogoutViewEntity
import com.castcle.android.presentation.setting.item_menu.SettingMenuViewEntity
import com.castcle.android.presentation.setting.item_notification.SettingNotificationViewEntity
import com.castcle.android.presentation.setting.item_profile.SettingProfileViewEntity
import com.castcle.android.presentation.setting.item_profile_section.SettingProfileSectionViewEntity
import com.castcle.android.presentation.setting.item_verify_email.SettingVerifyEmailViewEntity
import org.koin.core.annotation.Factory

@Factory
class SettingMapper {

    fun map(
        response: List<UserWithSyncSocialEntity>,
        notification: List<NotificationBadgesEntity>
    ): List<CastcleViewEntity> {
        val pageProfile = response.filter { it.user.type is UserType.Page }
        val userProfile = response.firstOrNull { it.user.type is UserType.People }
            ?: UserWithSyncSocialEntity()
        val notificationItems = notification.map { map ->
            SettingNotificationViewEntity(item = map)
        }.ifEmpty {
            listOf(SettingNotificationViewEntity())
        }
        val verifyEmailItems = if (
            !userProfile.user.email.isNullOrBlank() && !userProfile.user.verifiedEmail
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
        )
        return notificationItems.plus(verifyEmailItems)
            .plus(profileItems)
            .plus(menuItems)
            .plus(SettingLogoutViewEntity())
    }

}