package com.castcle.android.presentation.setting.setting

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.notification.NotificationRepository
import com.castcle.android.domain.user.UserRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingViewModel(
    private val authenticationRepository: AuthenticationRepository,
    database: CastcleDatabase,
    private val mapper: SettingMapper,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val logoutComplete = MutableLiveData<Unit>()

    val logoutError = MutableLiveData<Throwable>()

    init {
        fetchNotificationBadges()
        fetchUserPage()
        fetchUserProfile()
    }

    val views = database.user().retrieveWithSyncSocial()
        .combine(database.notificationBadges().retrieve(), mapper::map)
        .distinctUntilChanged()

    private fun fetchNotificationBadges() {
        launch {
            notificationRepository.fetchNotificationsBadges()
        }
    }

    private fun fetchUserPage() {
        launch {
            userRepository.fetchUserPage()
        }
    }

    private fun fetchUserProfile() {
        launch {
            userRepository.fetchUserProfile()
        }
    }

    fun logout() {
        launch(onError = logoutError::postValue) {
            authenticationRepository.unregisterFirebaseMessagingToken()
            authenticationRepository.loginOut()
            logoutComplete.postValue(Unit)
        }
    }

}