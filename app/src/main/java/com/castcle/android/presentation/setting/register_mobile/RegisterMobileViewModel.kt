package com.castcle.android.presentation.setting.register_mobile

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.presentation.setting.register_mobile.item_register_mobile.RegisterMobileViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RegisterMobileViewModel(
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData(listOf(RegisterMobileViewEntity()))

    fun requestOtpMobile(dialCode: String, mobileNumber: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) },
        ) {
            val otp = OtpEntity(
                countryCode = dialCode,
                mobileNumber = mobileNumber,
                objective = OtpObjective.VerifyMobile,
            )
            repository.requestOtpMobile(otp = otp)
        }
    }

}