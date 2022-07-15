package com.castcle.android.presentation.who_to_follow

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_DEFAULT
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.user.data_source.WhoToFollowRemoteMediator
import com.castcle.android.presentation.who_to_follow.item_who_to_follow.WhoToFollowViewEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WhoToFollowViewModel(
    api: UserApi,
    private val database: CastcleDatabase,
) : BaseViewModel() {

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = Pager(
        config = PagingConfig(
            initialLoadSize = PARAMETER_MAX_RESULTS_DEFAULT,
            pageSize = PARAMETER_MAX_RESULTS_DEFAULT,
        ), pagingSourceFactory = {
            database.whoToFollow().pagingSource()
        }, remoteMediator = WhoToFollowRemoteMediator(
            api = api,
            database = database,
        )
    ).flow.map { pagingData ->
        pagingData.map { WhoToFollowViewEntity.map(it) }
    }.cachedIn(viewModelScope)

}