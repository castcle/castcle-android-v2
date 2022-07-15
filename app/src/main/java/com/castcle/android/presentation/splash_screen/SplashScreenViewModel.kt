package com.castcle.android.presentation.splash_screen

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.type.AccessTokenType
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashScreenViewModel(
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val fetchAccessTokenComplete = MutableLiveData<Unit>()

    init {
        fetchAccessToken()
    }

    private fun fetchAccessToken() {
        launch {
            if (repository.getAccessToken().type is AccessTokenType.Guest) {
                repository.fetchGuestAccessToken()
            }
            fetchAccessTokenComplete.postValue(Unit)
        }
    }

}