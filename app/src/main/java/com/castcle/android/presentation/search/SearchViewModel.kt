package com.castcle.android.presentation.search

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.search.entity.RecentSearchEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchViewModel(
    private val database: CastcleDatabase,
) : BaseViewModel() {

    fun updateRecentSearch(keyword: String) {
        launch {
            database.recentSearch().insert(RecentSearchEntity(keyword = keyword))
        }
    }

}