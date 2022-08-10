package com.castcle.android.presentation.home

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val database: CastcleDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

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
            if (database.accessToken().get().firstOrNull()?.isGuest() == false) {
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

    fun showReportingContent(contentId: List<String>) {
        launch {
            database.cast().updateReporting(contentId, false)
        }
    }

}