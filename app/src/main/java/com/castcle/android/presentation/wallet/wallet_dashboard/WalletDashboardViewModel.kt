package com.castcle.android.presentation.wallet.wallet_dashboard

import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import androidx.room.withTransaction
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.RetryException
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.domain.wallet.entity.WalletDashboardEntity
import com.castcle.android.domain.wallet.type.WalletDashboardType
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletDashboardViewModel(
    private val database: CastcleDatabase,
    private val mapper: WalletDashboardMapper,
    private val repository: WalletRepository,
) : BaseViewModel() {

    val filter = MutableStateFlow<WalletHistoryFilter>(WalletHistoryFilter.WalletBalance)

    val loadState = MutableSharedFlow<LoadState>()

    val views = MutableLiveData<List<CastcleViewEntity>>()

    val userId = MutableStateFlow<String?>(null)

    init {
        startOnDataChangeFlow()
        startOnFilterChangeFlow()
        startOnUserChangeFlow()
    }

    private fun fetchData(filter: WalletHistoryFilter, userId: String) {
        launch(onError = {
            loadState.emitOnSuspend(RetryException.loadState(it) { fetchData(filter, userId) })
        }, onLaunch = {
            loadState.emitOnSuspend(LoadState.Loading)
        }, onSuccess = {
            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
        }) {
            val walletItems = WalletDashboardEntity(
                filter = filter,
                relationId = repository.getWalletBalance(userId).userId,
            )
            val historyItems = repository.getWalletHistory(filter.id, userId).map {
                WalletDashboardEntity(
                    createAt = it.createdAt,
                    filter = filter,
                    relationId = it.id,
                    type = WalletDashboardType.History,
                )
            }.ifEmpty {
                listOf(getHistoryItems(WalletDashboardType.Empty))
            }
            database.withTransaction {
                database.walletDashboard().delete()
                database.walletDashboard().insert(listOf(walletItems).plus(historyItems))
            }
        }
    }

    private fun fetchHistory(filter: WalletHistoryFilter, userId: String) {
        launch(onError = {
            val items = getHistoryItems(WalletDashboardType.Error).copy(
                relationId = it.message.orEmpty(),
            )
            updateHistoryItems(items)
        }, onLaunch = {
            updateHistoryItems(getHistoryItems(WalletDashboardType.Loading))
        }) {
            val historyItems = repository.getWalletHistory(filter.id, userId).map {
                WalletDashboardEntity(
                    createAt = it.createdAt,
                    filter = filter,
                    relationId = it.id,
                    type = WalletDashboardType.History,
                )
            }.ifEmpty {
                listOf(getHistoryItems(WalletDashboardType.Empty))
            }
            database.withTransaction {
                database.walletDashboard().deleteExcludeType(WalletDashboardType.Balance)
                database.walletDashboard().insert(historyItems)
            }
        }
    }

    fun getAccessToken(action: (String) -> Unit) {
        launch {
            action(database.accessToken().get()?.accessToken.orEmpty())
        }
    }

    private fun getHistoryItems(type: WalletDashboardType): WalletDashboardEntity {
        return WalletDashboardEntity(
            createAt = Long.MIN_VALUE,
            filter = filter.value,
            type = type,
        )
    }

    private fun startOnFilterChangeFlow() {
        launch {
            filter.drop(1).distinctUntilChangedBy { it }.onEach {
                database.walletDashboard().updateFilter(it)
            }.collectLatest {
                fetchHistory(filter = it, userId = userId.value.orEmpty())
            }
        }
    }

    fun refreshData() {
        fetchData(filter = filter.value, userId = userId.value.orEmpty())
    }

    private fun startOnDataChangeFlow() {
        launch {
            val retryAction = {
                fetchHistory(filter = filter.value, userId = userId.value.orEmpty())
            }
            database.walletDashboard().retrieve()
                .map { mapper.apply(it, retryAction) }
                .collectLatest(views::postValue)
        }
    }

    private fun startOnUserChangeFlow() {
        launch {
            userId.value = database.user().get(UserType.People).firstOrNull()?.id
            userId.filterNotNull().distinctUntilChangedBy { it }.collectLatest {
                fetchData(filter = filter.value, userId = it)
            }
        }
    }

    private fun updateHistoryItems(items: WalletDashboardEntity) {
        launch {
            database.withTransaction {
                database.walletDashboard().deleteExcludeType(WalletDashboardType.Balance)
                database.walletDashboard().insert(listOf(items))
            }
        }
    }

}