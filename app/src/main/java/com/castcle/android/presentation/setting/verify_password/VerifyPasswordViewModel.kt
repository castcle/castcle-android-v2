package com.castcle.android.presentation.setting.verify_password

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.verify_password.item_verify_password.VerifyPasswordViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class VerifyPasswordViewModel(
    private val database: CastcleDatabase,
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData(listOf(VerifyPasswordViewEntity()))

    fun verifyPassword(password: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) }
        ) {
            val user = database.user().get(UserType.People).firstOrNull()
            val otp = OtpEntity(
                email = user?.email.orEmpty(),
                objective = OtpObjective.ChangePassword,
                password = password,
                type = OtpType.Password,
            )
            repository.verifyOtp(otp)
        }
    }

}