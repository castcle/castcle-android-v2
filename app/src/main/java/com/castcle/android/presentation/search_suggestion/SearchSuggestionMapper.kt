package com.castcle.android.presentation.search_suggestion

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.search.entity.RecentSearchEntity
import com.castcle.android.domain.search.entity.SearchSuggestionEntity
import com.castcle.android.presentation.search_suggestion.item_keyword.SearchSuggestionKeywordViewEntity
import com.castcle.android.presentation.search_suggestion.item_title.SearchSuggestionTitleViewEntity
import com.castcle.android.presentation.search_suggestion.item_user.SearchSuggestionUserViewEntity
import org.koin.core.annotation.Factory

@Factory
class SearchSuggestionMapper {

    fun map(
        recentSearch: List<RecentSearchEntity> = listOf(),
        searchSuggestion: SearchSuggestionEntity = SearchSuggestionEntity(),
    ): List<CastcleViewEntity> {
        return if (searchSuggestion.keyword.isEmpty()) {
            val title = listOf(SearchSuggestionTitleViewEntity())
            val recent = recentSearch.map {
                SearchSuggestionKeywordViewEntity(
                    name = it.keyword,
                    slug = it.keyword,
                    uniqueId = it.keyword,
                )
            }
            title.plus(recent)
        } else {
            val keyword = searchSuggestion.keyword.map {
                SearchSuggestionKeywordViewEntity(
                    name = it.text,
                    slug = it.text,
                    uniqueId = it.text,
                )
            }
            val hashtag = searchSuggestion.hashtag.map {
                SearchSuggestionKeywordViewEntity(
                    name = "#${it.name}",
                    slug = it.slug,
                    uniqueId = it.slug,
                )
            }
            val user = searchSuggestion.user.map {
                SearchSuggestionUserViewEntity(
                    uniqueId = it.id,
                    user = it,
                )
            }
            keyword.plus(hashtag).plus(user)
        }
    }

}