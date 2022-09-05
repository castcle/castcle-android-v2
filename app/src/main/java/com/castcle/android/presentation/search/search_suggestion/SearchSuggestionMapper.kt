/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.search.search_suggestion

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.search.entity.RecentSearchEntity
import com.castcle.android.domain.search.entity.SearchSuggestionEntity
import com.castcle.android.presentation.search.search_suggestion.item_keyword.SearchSuggestionKeywordViewEntity
import com.castcle.android.presentation.search.search_suggestion.item_title.SearchSuggestionTitleViewEntity
import com.castcle.android.presentation.search.search_suggestion.item_user.SearchSuggestionUserViewEntity
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