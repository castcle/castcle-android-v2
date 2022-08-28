package com.castcle.android.presentation.setting.change_password

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.presentation.setting.change_password.item_change_password.ChangePasswordViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ChangePasswordViewModel(
    private val otp: OtpEntity,
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData(listOf(ChangePasswordViewEntity()))

    fun changePassword(password: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(otp) },
        ) {
            repository.changePassword(otp.copy(password = password))
        }
    }

}