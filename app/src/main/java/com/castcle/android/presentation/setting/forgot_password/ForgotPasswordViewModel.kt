package com.castcle.android.presentation.setting.forgot_password

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.presentation.setting.forgot_password.item_forgot_password.ForgotPasswordViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ForgotPasswordViewModel(
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData(listOf(ForgotPasswordViewEntity()))

    fun forgotPassword(email: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) }
        ) {
            val otp = OtpEntity(
                email = email,
                objective = OtpObjective.ForgotPassword,
                type = OtpType.Email,
            )
            repository.requestOtp(otp)
        }
    }

}