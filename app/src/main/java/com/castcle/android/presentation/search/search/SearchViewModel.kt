package com.castcle.android.presentation.search.search

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.search.entity.RecentSearchEntity
import com.castcle.android.domain.search.entity.SearchKeywordEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchViewModel(
    private val database: CastcleDatabase,
    private val sessionId: Long,
) : BaseViewModel() {

    fun updateRecentSearch(keyword: String) {
        launch {
            val item = RecentSearchEntity(keyword = keyword)
            database.recentSearch().insert(item)
        }
    }

    fun updateSearchKeyword(keyword: String) {
        launch {
            val item = SearchKeywordEntity(keyword = keyword, sessionId = sessionId)
            database.searchKeyword().insert(item)
        }
    }

}