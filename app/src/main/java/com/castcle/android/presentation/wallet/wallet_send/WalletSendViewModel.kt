package com.castcle.android.presentation.wallet.wallet_send

import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.RetryException
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewEntity
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletSendViewModel(
    private val database: CastcleDatabase,
    private val mapper: WalletSendMapper,
    private val parameter: WalletSendViewModelParameter,
    private val repository: WalletRepository,
) : BaseViewModel() {

    val loadState = MutableSharedFlow<LoadState>()

    val views = MutableLiveData<WalletSendViewEntity>()

    init {
        fetchData()
        fetchWalletShortcut()
    }

    private fun fetchWalletShortcut() {
        launch(onError = {
            loadState.emitOnSuspend(RetryException.loadState(it) { fetchWalletShortcut() })
        }, onLaunch = {
            loadState.emitOnSuspend(LoadState.Loading)
        }, onSuccess = {
            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
        }) {
            repository.getWalletShortcuts(userId = parameter.userId)
        }
    }

    private fun fetchData() {
        launch {
            database.user().retrieve(UserType.People)
                .combine(database.user().retrieve(UserType.Page)) { people, page ->
                    people.plus(page)
                }.combine(database.walletShortcut().retrieve()) { user, shortcut ->
                    mapper.apply(user, shortcut, parameter)
                }.combine(database.walletBalance().retrieve(parameter.userId)) { item, balance ->
                    item.copy(balance = balance?.availableBalance?.toDoubleOrNull() ?: 0.0)
                }.distinctUntilChanged().collectLatest {
                    views.postValue(it)
                }
        }
    }

    data class WalletSendViewModelParameter(
        val targetCastcleId: String? = null,
        val targetUserId: String? = null,
        val userId: String = "",
    )

}