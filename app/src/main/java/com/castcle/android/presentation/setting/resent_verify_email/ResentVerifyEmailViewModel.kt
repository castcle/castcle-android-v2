package com.castcle.android.presentation.setting.resent_verify_email

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.resent_verify_email.item_resent_verify_email.ResentVerifyEmailViewEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ResentVerifyEmailViewModel(
    database: CastcleDatabase,
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableLiveData<Throwable>()

    val onSuccess = MutableLiveData<Unit>()

    val views = database.user().retrieve(UserType.People)
        .mapNotNull { it.firstOrNull()?.email?.ifBlank { null } }
        .map { ResentVerifyEmailViewEntity(email = it) }

    fun resentVerifyEmail() {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            repository.resentVerifyEmail()
        }
    }

}