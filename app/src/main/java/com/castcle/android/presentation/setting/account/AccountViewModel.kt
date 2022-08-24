package com.castcle.android.presentation.setting.account

import androidx.lifecycle.MutableLiveData
import com.castcle.android.R
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.account.item_menu.AccountMenuViewEntity
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewEntity
import com.twitter.sdk.android.core.TwitterAuthToken
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AccountViewModel(
    database: CastcleDatabase,
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val onError = MutableLiveData<Throwable>()

    val onSuccess = MutableLiveData<Unit>()

    val views = database.user().retrieveWithLinkSocial(UserType.People)
        .filterNotNull()
        .map { result ->
            listOf(
                AccountTitleViewEntity(titleId = R.string.fragment_account_title_1),
                AccountMenuViewEntity(
                    action = {
                        if (!result.user.verifiedEmail) {
                            it.onResentVerifyEmailClicked()
                        }
                    },
                    description = if (result.user.email.isNullOrBlank() || result.user.verifiedEmail) {
                        null
                    } else {
                        R.string.not_verify
                    },
                    detail = result.user.email?.ifBlank { null } ?: R.string.unregistered,
                    showArrow = !result.user.verifiedEmail,
                    titleId = R.string.email,
                ),
                AccountMenuViewEntity(
                    action = { it.onMobileNumberClicked() },
                    detail = if (result.user.mobileCountryCode.isNullOrBlank() || result.user.mobileNumber.isNullOrBlank()) {
                        R.string.unregistered
                    } else {
                        "(${result.user.mobileCountryCode}) ${result.user.mobileNumber}"
                    },
                    titleId = R.string.mobile_number,
                ),
                AccountMenuViewEntity(
                    action = { it.onPasswordClicked() },
                    detail = if (result.user.passwordNotSet == false) {
                        R.string.registered
                    } else {
                        R.string.unregistered
                    },
                    detailColor = if (result.user.passwordNotSet == false) {
                        R.color.blue
                    } else {
                        R.color.gray_1
                    },
                    titleId = R.string.password,
                ),
                AccountTitleViewEntity(titleId = R.string.fragment_account_title_2),
                AccountMenuViewEntity(
                    action = {
                        if (result.linkSocial.find { find -> find.provider is SocialType.Facebook } == null) {
                            it.onLinkFacebookClicked()
                        }
                    },
                    detail = if (result.linkSocial.find { it.provider is SocialType.Facebook } != null) {
                        R.string.linked
                    } else {
                        R.string.link
                    },
                    detailColor = if (result.linkSocial.find { it.provider is SocialType.Facebook } != null) {
                        R.color.blue
                    } else {
                        R.color.gray_1
                    },
                    imageColor = R.color.blue_facebook,
                    imageIcon = R.drawable.ic_facebook,
                    titleId = R.string.facebook,
                ),
                AccountMenuViewEntity(
                    action = {
                        if (result.linkSocial.find { find -> find.provider is SocialType.Twitter } == null) {
                            it.onLinkTwitterClicked()
                        }
                    },
                    detail = if (result.linkSocial.find { it.provider is SocialType.Twitter } != null) {
                        R.string.linked
                    } else {
                        R.string.link
                    },
                    detailColor = if (result.linkSocial.find { it.provider is SocialType.Twitter } != null) {
                        R.color.blue
                    } else {
                        R.color.gray_1
                    },
                    imageColor = R.color.blue_twitter,
                    imageIcon = R.drawable.ic_twitter,
                    titleId = R.string.twitter,
                ),
                AccountTitleViewEntity(titleId = R.string.fragment_account_title_3),
                AccountMenuViewEntity(
                    action = { it.onDeleteAccountClicked() },
                    titleId = R.string.delete_account,
                ),
            )
        }

    fun linkWithFacebook() {
        launch(onError = onError::postValue) {
            repository.linkWithFacebook()
            onSuccess.postValue(Unit)
        }
    }

    fun linkWithTwitter(token: TwitterAuthToken?) {
        launch(onError = onError::postValue) {
            repository.linkWithTwitter(token)
            onSuccess.postValue(Unit)
        }
    }

}