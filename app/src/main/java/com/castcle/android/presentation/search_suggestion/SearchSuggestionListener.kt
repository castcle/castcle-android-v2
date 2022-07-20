package com.castcle.android.presentation.search_suggestion

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity

interface SearchSuggestionListener : CastcleListener {
    fun onClearRecentSearch()
    fun onKeywordClicked(keyword: String)
    fun onUserClicked(user: UserEntity)
}