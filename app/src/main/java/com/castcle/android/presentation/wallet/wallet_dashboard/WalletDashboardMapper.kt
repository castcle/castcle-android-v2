package com.castcle.android.presentation.wallet.wallet_dashboard

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.custom_view.load_state.item_error_append.ErrorAppendViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_append.LoadingAppendViewEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.*
import com.castcle.android.domain.wallet.type.WalletDashboardType
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance.WalletDashboardBalanceViewEntity
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty.WalletDashboardEmptyViewEntity
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history.WalletDashboardHistoryViewEntity
import org.koin.core.annotation.Factory

@Factory
class WalletDashboardMapper {

    fun apply(
        response: List<WalletDashboardWithResultEntity>,
        retryAction: () -> Unit,
    ): List<CastcleViewEntity> {
        return response.map { map ->
            when (map.dashboard.type) {
                is WalletDashboardType.Balance -> WalletDashboardBalanceViewEntity(
                    balance = map.balance ?: WalletBalanceEntity(),
                    filter = map.dashboard.filter,
                    user = map.user ?: UserEntity(),
                )
                is WalletDashboardType.Empty -> WalletDashboardEmptyViewEntity(
                    filter = map.dashboard.filter,
                )
                is WalletDashboardType.Error -> ErrorAppendViewEntity(
                    action = retryAction,
                    errorMessage = map.dashboard.relationId,
                )
                is WalletDashboardType.Loading -> LoadingAppendViewEntity()
                is WalletDashboardType.History -> WalletDashboardHistoryViewEntity(
                    history = map.history ?: WalletHistoryEntity(),
                    uniqueId = map.history?.id.orEmpty(),
                )
            }
        }
    }

}