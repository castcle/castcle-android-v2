package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog.WalletDashboardDialogFilterViewEntity
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog.WalletDashboardDialogUserViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletDashboardDialogViewModel(
    private val database: CastcleDatabase,
) : BaseViewModel() {

    val views = MutableLiveData<List<CastcleViewEntity>>()

    fun getItems(currentFilter: WalletHistoryFilter?, currentUserId: String?) {
        launch(onSuccess = {
            views.postValue(it)
        }) {
            if (currentFilter != null) {
                listOf(
                    WalletHistoryFilter.WalletBalance,
                    WalletHistoryFilter.ContentFarming,
                    WalletHistoryFilter.SocialRewards,
                    WalletHistoryFilter.DepositSend,
                    WalletHistoryFilter.AirdropReferral,
                ).map {
                    WalletDashboardDialogFilterViewEntity(
                        selected = currentFilter == it,
                        filter = it,
                        uniqueId = it.id,
                    )
                }
            } else {
                database.user().get(UserType.People)
                    .plus(database.user().get(UserType.Page))
                    .map {
                        WalletDashboardDialogUserViewEntity(
                            selected = it.id == currentUserId,
                            user = it,
                            uniqueId = it.id,
                        )
                    }
            }
        }
    }

}