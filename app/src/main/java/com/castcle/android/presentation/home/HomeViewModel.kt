package com.castcle.android.presentation.home

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import kotlinx.coroutines.flow.mapNotNull
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val recursiveRefreshToken = database.recursiveRefreshToken()
        .retrieve()
        .mapNotNull { it.firstOrNull() }

    fun followUser(targetUser: UserEntity) {
        launch {
            isNotGuest {
                if (targetUser.followed) {
                    userRepository.unfollowUser(targetUser = targetUser)
                } else {
                    userRepository.followUser(targetUser = targetUser)
                }
            }
        }
    }

    private fun isNotGuest(action: suspend () -> Unit) {
        launch {
            if (database.accessToken().get()?.isGuest() == false) {
                action()
            }
        }
    }

    fun likeCasts(targetCasts: CastEntity) {
        launch {
            isNotGuest {
                if (targetCasts.liked) {
                    userRepository.unlikeCasts(content = targetCasts)
                } else {
                    userRepository.likeCasts(content = targetCasts)
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

    fun showReportingContent(contentId: List<String>) {
        launch {
            database.cast().updateReporting(contentId, false)
        }
    }

}