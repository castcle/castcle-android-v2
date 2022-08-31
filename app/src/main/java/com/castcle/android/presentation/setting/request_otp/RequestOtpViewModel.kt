package com.castcle.android.presentation.setting.request_otp

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.request_otp.item_request_otp_email.RequestOtpEmailViewEntity
import com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile.RequestOtpMobileViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RequestOtpViewModel(
    private val database: CastcleDatabase,
    private val repository: AuthenticationRepository,
    private val type: OtpType,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData<CastcleViewEntity>()

    init {
        getItems()
    }

    private fun getItems() {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { views.postValue(it) },
        ) {
            when (type) {
                is OtpType.Email, is OtpType.Password -> database.user().get(UserType.People)
                    .firstOrNull()
                    .let { RequestOtpEmailViewEntity(email = it?.email.orEmpty()) }
                is OtpType.Mobile -> RequestOtpMobileViewEntity()
            }
        }
    }

    fun requestOtp(countryCode: String, email: String, mobileNumber: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) },
        ) {
            val otp = OtpEntity(
                countryCode = countryCode,
                email = email,
                mobileNumber = mobileNumber,
                objective = when (type) {
                    is OtpType.Email, is OtpType.Password -> OtpObjective.ChangePassword
                    is OtpType.Mobile -> OtpObjective.VerifyMobile
                },
                type = type,
            )
            repository.requestOtp(otp = otp)
        }
    }

}