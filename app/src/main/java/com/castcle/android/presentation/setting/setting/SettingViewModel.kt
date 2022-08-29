package com.castcle.android.presentation.setting.setting

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.timer
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.notification.NotificationRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.type.UserType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val mapper: SettingMapper,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val logoutComplete = MutableLiveData<Unit>()

    val logoutError = MutableLiveData<Throwable>()

    private var userUpdater: Job? = null

    init {
        startUserUpdater()
    }

    val views = database.user().retrieveWithSyncSocial()
        .combine(database.notificationBadges().retrieve(), mapper::map)
        .distinctUntilChanged()

    fun fetchData() {
        fetchNotificationBadges()
        fetchUserPage()
        fetchUserProfile()
    }

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

    private fun startUserUpdater() {
        userUpdater = launch {
            timer(delay = 5_000).collectLatest {
                if (database.user().get(UserType.People).firstOrNull()?.isNotVerified() == true) {
                    fetchUserProfile()
                } else {
                    userUpdater?.cancel()
                }
            }
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