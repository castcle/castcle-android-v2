package com.castcle.android.presentation.login

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.data.authentication.entity.LoginWithEmailRequest
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.presentation.login.item_login.LoginViewEntity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.twitter.sdk.android.core.TwitterAuthToken
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val loginComplete = MutableLiveData<Unit>()

    val loginError = MutableLiveData<Throwable>()

    val items = MutableLiveData<List<CastcleViewEntity>>(listOf(LoginViewEntity()))

    init {
        logoutFacebook()
    }

    fun loginWithEmail(email: String, password: String) {
        launch(onError = loginError::postValue) {
            repository.loginWithEmail(LoginWithEmailRequest(email, password))
            loginComplete.postValue(Unit)
        }
    }

    fun loginWithFacebook() {
        launch(onError = {
            loginError.postValue(it)
            logoutFacebook()
        }, onSuccess = {
            loginComplete.postValue(Unit)
            logoutFacebook()
        }) {
            repository.loginWithFacebook()
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount) {
        launch(onError = loginError::postValue) {
            repository.loginWithGoogle(account)
            loginComplete.postValue(Unit)
        }
    }

    fun loginWithTwitter(token: TwitterAuthToken?) {
        launch(onError = loginError::postValue) {
            repository.loginWithTwitter(token)
            loginComplete.postValue(Unit)
        }
    }

    fun logoutFacebook() {
        launch {
            repository.loginWithFacebook()
        }
    }

}