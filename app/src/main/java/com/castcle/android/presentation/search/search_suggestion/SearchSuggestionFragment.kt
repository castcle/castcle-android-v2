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

import android.os.Bundle
import android.view.*
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.hideKeyboard
import com.castcle.android.databinding.LayoutRecyclerViewWithSearchBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.search.search_suggestion.item_keyword.SearchSuggestionKeywordViewRenderer
import com.castcle.android.presentation.search.search_suggestion.item_title.SearchSuggestionTitleViewEntity
import com.castcle.android.presentation.search.search_suggestion.item_title.SearchSuggestionTitleViewRenderer
import com.castcle.android.presentation.search.search_suggestion.item_user.SearchSuggestionUserViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class SearchSuggestionFragment : BaseFragment(), SearchSuggestionListener {

    private val viewModel by stateViewModel<SearchSuggestionViewModel>()

    override fun initViewProperties() {
        if (binding.recyclerView.adapter == null) {
            binding.searchBar.focus()
        }
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.for_you,
            titleColor = R.color.blue,
        )
    }

    override fun initListener() {
        binding.searchBar.setSearchClickedListener { keyword ->
            hideKeyboard()
            binding.searchBar.clearFocus()
            if (keyword.isNotBlank()) {
                hideKeyboard()
                SearchSuggestionFragmentDirections.toSearchFragment(keyword).navigate()
            }
        }
        binding.searchBar.setTextChangeListener { keyword ->
            viewModel.search(keyword)
        }
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onClearRecentSearch() {
        viewModel.clearRecentSearch()
    }

    override fun onKeywordClicked(keyword: String) {
        hideKeyboard()
        SearchSuggestionFragmentDirections.toSearchFragment(keyword).navigate()
    }

    override fun onUserClicked(user: UserEntity) {
        hideKeyboard()
        SearchSuggestionFragmentDirections.toProfileFragment(user).navigate()
    }

    override fun onPause() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onPause()
    }

    override fun onResume() {
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
        super.onResume()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(SearchSuggestionKeywordViewRenderer())
            registerRenderer(SearchSuggestionTitleViewRenderer())
            registerRenderer(SearchSuggestionUserViewRenderer())
            submitList(listOf(SearchSuggestionTitleViewEntity()))
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewWithSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}