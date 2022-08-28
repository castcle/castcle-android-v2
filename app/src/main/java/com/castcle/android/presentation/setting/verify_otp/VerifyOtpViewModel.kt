package com.castcle.android.presentation.setting.verify_otp

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.presentation.setting.verify_otp.item_verify_otp.VerifyOtpViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class VerifyOtpViewModel(
    otp: OtpEntity,
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onResentOtpSuccess = MutableSharedFlow<Unit>()

    val onUpdateMobileNumberSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData(listOf(VerifyOtpViewEntity(otp = otp)))

    fun requestOtpMobile(otp: OtpEntity) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onResentOtpSuccess.emitOnSuspend(Unit) }
        ) {
            val items = views.value
                ?.map { it.copy(otp = repository.requestOtpMobile(otp = otp)) }
                ?: listOf(VerifyOtpViewEntity())
            views.postValue(items)
        }
    }

    fun updateMobileNumber(otp: OtpEntity) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onUpdateMobileNumberSuccess.emitOnSuspend(otp) }
        ) {
            repository.updateMobileNumber(otp)
        }
    }

}