package com.castcle.android.presentation.wallet.wallet_deposit

import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.RetryException
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.presentation.wallet.wallet_deposit.item_wallet_deposit.WalletDepositViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletDepositViewModel(
    private val database: CastcleDatabase,
    private val repository: WalletRepository,
    private val userId: String,
) : BaseViewModel() {

    val loadState = MutableSharedFlow<LoadState>()

    val views = MutableLiveData<WalletDepositViewEntity>()

    init {
        getMyQrCode()
    }

    private fun getMyQrCode() {
        launch(onError = {
            loadState.emitOnSuspend(RetryException.loadState(it) { getMyQrCode() })
        }, onLaunch = {
            loadState.emitOnSuspend(LoadState.Loading)
        }, onSuccess = {
            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
        }) {
            val castcleId = database.user().get(userId).firstOrNull()?.castcleId.orEmpty()
            val qrCode = repository.getMyQrCode(userId)
            val items = WalletDepositViewEntity(
                castcleId = castcleId,
                qrCode = qrCode,
                userId = userId,
            )
            views.postValue(items)
        }
    }

}