package com.castcle.android.presentation.search_suggestion

import android.os.Bundle
import android.view.*
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.hideKeyboard
import com.castcle.android.databinding.FragmentSearchSuggestionBinding
import com.castcle.android.presentation.top_trends.item_top_trends_item.TopTrendsItemViewRenderer
import com.castcle.android.presentation.top_trends.item_top_trends_search.TopTrendsSearchViewRenderer
import com.castcle.android.presentation.top_trends.item_top_trends_title.TopTrendsTitleViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class SearchSuggestionFragment : BaseFragment(), SearchSuggestionListener {

    private val viewModel by stateViewModel<SearchSuggestionViewModel>()

    override fun initViewProperties() {
        binding.searchBar.focus()
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
                Timber.e("KUY : setSearchClickedListener, $keyword")
            }
        }
        binding.searchBar.setTextChangeListener { keyword ->
            Timber.e("KUY : setTextChangeListener, $keyword")
        }
    }

    override fun initObserver() {

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
            registerRenderer(TopTrendsTitleViewRenderer())
            registerRenderer(TopTrendsSearchViewRenderer())
            registerRenderer(TopTrendsItemViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentSearchSuggestionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}