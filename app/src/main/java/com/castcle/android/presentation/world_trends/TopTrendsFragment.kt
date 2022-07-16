package com.castcle.android.presentation.world_trends

import android.os.Bundle
import android.view.*
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.world_trends.item_top_trends_item.TopTrendsItemViewRenderer
import com.castcle.android.presentation.world_trends.item_top_trends_search.TopTrendsSearchViewRenderer
import com.castcle.android.presentation.world_trends.item_top_trends_title.TopTrendsTitleViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class TopTrendsFragment : BaseFragment(), TopTrendsListener {

    private val viewModel by stateViewModel<TopTrendsViewModel>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonIcon = R.drawable.ic_castcle,
            leftButtonAction = { scrollToTop() },
            title = R.string.for_you,
            titleColor = R.color.blue,
        )
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onSearchClicked() {

    }

    override fun onTrendClicked(keyword: String) {

    }

    fun scrollToTop() {
        binding.recyclerView.scrollToPosition(0)
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
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance() = TopTrendsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}