package com.castcle.android.presentation.home

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val isGuest = MutableStateFlow(false)

    private val isUserNotVerified = MutableStateFlow(true)

    val recursiveRefreshToken = database.recursiveRefreshToken()
        .retrieve()
        .mapNotNull { it.firstOrNull() }

    init {
        isGuestUpdater()
        isUserVerifiedUpdater()
    }

    fun followUser(
        isGuestAction: () -> Unit,
        targetUser: UserEntity,
    ) {
        isUserCanEngagement(isGuestAction = isGuestAction) {
            launch {
                if (targetUser.followed) {
                    userRepository.unfollowUser(targetUser = targetUser)
                } else {
                    userRepository.followUser(targetUser = targetUser)
                }
            }
        }
    }

    private fun isGuestUpdater() {
        launch {
            database.accessToken().retrieve()
                .mapNotNull { it.firstOrNull() }
                .collectLatest { isGuest.value = it.isGuest() }
        }
    }

    fun isUserCanEngagement(
        isGuestAction: (() -> Unit)? = null,
        isUserNotVerifiedAction: (() -> Unit)? = null,
        isMemberAction: () -> Unit,
    ) {
        when {
            isGuestAction != null && isGuest.value -> isGuestAction()
            isUserNotVerifiedAction != null && isUserNotVerified.value -> isUserNotVerifiedAction()
            else -> isMemberAction()
        }
    }

    private fun isUserVerifiedUpdater() {
        launch {
            database.user().retrieve(UserType.People)
                .mapNotNull { it.firstOrNull() }
                .collectLatest { isUserNotVerified.value = it.isNotVerified() }
        }
    }

    fun likeCast(
        isGuestAction: () -> Unit = {},
        isUserNotVerifiedAction: () -> Unit = {},
        targetCast: CastEntity,
    ) {
        isUserCanEngagement(
            isGuestAction = isGuestAction,
            isUserNotVerifiedAction = isUserNotVerifiedAction,
        ) {
            launch {
                if (targetCast.liked) {
                    userRepository.unlikeCasts(content = targetCast)
                } else {
                    userRepository.likeCasts(content = targetCast)
                }
            }
        }
    }

    fun logout(
        onLaunchAction: () -> Unit,
        onSuccessAction: () -> Unit,
    ) {
        launch(
            onLaunch = { onLaunchAction() },
            onSuccess = { onSuccessAction() },
        ) {
            authenticationRepository.loginOut()
        }
    }

}