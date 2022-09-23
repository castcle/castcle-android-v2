@file:OptIn(DelicateCoroutinesApi::class)

package com.castcle.android.presentation.setting.ads.boost_ads

import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.RetryException
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.ads.AdvertiseRepository
import com.castcle.android.domain.ads.entity.BoostAdRequest
import com.castcle.android.domain.ads.entity.BoostAdsEntity
import com.castcle.android.domain.ads.type.*
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_ad_page.ItemPreviewAdPageViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_error.ItemErrorViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_budget.ItemBudgetViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_campaign.ItemCampaignViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_objective.ChooseObjectiveViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_page_boost.ItemChoosePageViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_payment.ItemChoosePaymentViewEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 14/9/2022 AD at 15:08.

@KoinViewModel
class BoostAdsViewModel(
    private val database: CastcleDatabase,
    private var mapper: BoostAdsMapper,
    private val repository: WalletRepository,
    private val adRepository: AdvertiseRepository,
    private val state: SavedStateHandle
) : BaseViewModel() {

    val viewItem = MutableLiveData<List<CastcleViewEntity>>()

    private val cacheViewItem = MutableLiveData<List<CastcleViewEntity>>()

    val castId = MutableStateFlow<String?>(null)

    private val castWithUserEntity = MutableStateFlow<CastWithUserEntity?>(null)

    val userChange = MutableStateFlow<String?>(null)

    val boostType = MutableStateFlow<AdvertiseType?>(null)

    val loadState = MutableSharedFlow<LoadState>()

    val objectiveChange = MutableStateFlow<ObjectiveType?>(null)

    val paymentChange = MutableStateFlow<PaymentType?>(null)

    val statePreview = MutableStateFlow(false)

    val errorBoostAds = MutableStateFlow<List<String>?>(null)

    var uiState = MutableStateFlow<BaseUiState<Nothing>>(BaseUiState.Non)

    init {
        startOnDataChangeFlow()
        initUserFlow()
        initUserChange()
        initErrorBoostAds()
    }

    private fun initErrorBoostAds() {
        launch {
            errorBoostAds.filterNotNull()
                .distinctUntilChangedBy { it }
                .map {
                    it.map {
                        ItemErrorViewEntity(errorMessage = it)
                    }
                }.collectLatest {
                    viewItem.value?.toMutableList()?.apply {
                        addAll(it)
                    }.also {
                        viewModelScope.launch(Dispatchers.Main) {
                            viewItem.value = it
                        }
                    }
                }
        }
    }

    private fun initUserChange() {
        launch {
            userChange.filterNotNull()
                .distinctUntilChangedBy { it }
                .collect {
                    database.withTransaction {
                        database.boostAds().updateUserRelationId(it)
                    }
                }
        }

        launch {
            objectiveChange.filterNotNull()
                .distinctUntilChangedBy { it }.collectLatest {
                    database.withTransaction {
                        database.boostAds().updateObjective(it)
                    }
                }
        }

        launch {
            paymentChange.filterNotNull()
                .distinctUntilChangedBy { it }.collectLatest {
                    database.withTransaction {
                        database.boostAds().updatePayment(it)
                    }
                }
        }

        launch {
            castId.filterNotNull()
                .distinctUntilChangedBy { it }
                .collectLatest { it ->
                    database.cast().get(castId = it)?.let {
                        castWithUserEntity.value = it
                    }
                }
        }
    }

    private fun initUserFlow() {
        launch {
            userChange.value?.let {
                fetchItemView(userID = it)
            }
        }

        launch {
            userChange.filterNotNull()
                .distinctUntilChangedBy { it }
                .collect {
                    repository.getWalletBalance(it).userId
                }
        }
    }

    fun initDefaultValue() {
        GlobalScope.launch(Dispatchers.IO) {
            val userId = database.user().get(UserType.People).firstOrNull()?.id ?: ""
            val walletItems = BoostAdsEntity(
                objective = ObjectiveType.Engagement,
                paymentMethod = PaymentType.TokenBalance,
                relationId = repository.getWalletBalance(userId).userId,
                castRelationId = castId.value
            )
            database.withTransaction {
                database.boostAds().delete()
                database.boostAds().insert(listOf(walletItems))
            }
        }
    }

    private fun fetchItemView(userID: String) {
        launch(onError = {
            loadState.emitOnSuspend(RetryException.loadState(it))
        }, onLaunch = {
            loadState.emitOnSuspend(LoadState.Loading)
        }, onSuccess = {
            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
        }) {
            val walletItems = BoostAdsEntity(
                objective = ObjectiveType.Engagement,
                paymentMethod = PaymentType.TokenBalance,
                relationId = repository.getWalletBalance(userID).userId,
                castRelationId = castId.value
            )
            database.withTransaction {
                database.boostAds().delete()
                database.boostAds().insert(listOf(walletItems))
            }
        }
    }

    private fun startOnDataChangeFlow() {
        launch {
            database.boostAds()
                .retrieve()
                .map {
                    mapper.apply(
                        it.first(),
                        viewItem.value,
                        boostType.value ?: AdvertiseType.User
                    )
                }.collectLatest(viewItem::postValue)
        }
    }

    fun saveItemsState(layoutManager: RecyclerView.LayoutManager) {
        state[SAVE_STATE_RECYCLER_VIEW] = layoutManager.onSaveInstanceState()
    }

    fun restoreItemsState(layoutManager: RecyclerView.LayoutManager) {
        layoutManager.onRestoreInstanceState(state[SAVE_STATE_RECYCLER_VIEW])
    }

    fun onPreViewAdPage() {
        if (!statePreview.value) {
            val userEntitySelect =
                viewItem.value?.filterIsInstance<ItemChoosePageViewEntity>()?.map {
                    it.user
                }?.firstOrNull()

            val campaignViewEntity =
                viewItem.value?.filterIsInstance<ItemCampaignViewEntity>()?.map {
                    it
                }?.firstOrNull()

            statePreview.value = true
            when (boostType.value) {
                AdvertiseType.User -> {
                    ItemPreviewAdPageViewEntity(
                        userEntity = userEntitySelect ?: UserEntity(),
                        campaignMessage = campaignViewEntity?.campaignMessage ?: ""
                    )
                }
                else -> {
                    castWithUserEntity.value?.let {
                        mapper.applyPreview(it)
                    } ?: FeedTextViewEntity()
                }
            }.also {
                cacheViewItem.value = viewItem.value
                viewItem.value = listOf(it)
            }
        } else {
            val userEntitySelect =
                cacheViewItem.value?.filterIsInstance<ItemChoosePageViewEntity>()?.map {
                    it.user
                }?.firstOrNull()

            val campaignViewEntity =
                cacheViewItem.value?.filterIsInstance<ItemCampaignViewEntity>()?.map {
                    it
                }?.firstOrNull()

            val dailyBudgetViewEntity =
                cacheViewItem.value?.filterIsInstance<ItemBudgetViewEntity>()?.map {
                    it
                }?.firstOrNull()

            val paymentViewEntity =
                cacheViewItem.value?.filterIsInstance<ItemChoosePaymentViewEntity>()?.map {
                    it
                }?.firstOrNull()

            val objectiveViewEntity =
                cacheViewItem.value?.filterIsInstance<ChooseObjectiveViewEntity>()?.map {
                    it
                }?.firstOrNull()

            val request = BoostAdRequest(
                contentId = castId.value,
                castcleId = userEntitySelect?.castcleId,
                campaignMessage = campaignViewEntity?.campaignMessage?.ifBlank { null },
                campaignName = campaignViewEntity?.campaignName,
                dailyBudget = dailyBudgetViewEntity?.dailyBudget ?: 0.0,
                duration = dailyBudgetViewEntity?.durationDay ?: 0,
                objective = objectiveViewEntity?.objective?.id ?: ObjectiveType.Engagement.id,
                dailyBidType = DailyBidType.AutoBid.id,
                paymentMethod = paymentViewEntity?.paymentType?.id ?: PaymentType.TokenBalance.id
            )
            createBoostAd(request)
        }
    }

    private fun createBoostAd(request: BoostAdRequest) {
        launch {
            adRepository.createBoostPage(request).collectLatest {
                uiState.emit(it)
            }
        }
    }

    fun onBoostAdView() {
        statePreview.value = false
        viewItem.value = cacheViewItem.value
    }

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }
}