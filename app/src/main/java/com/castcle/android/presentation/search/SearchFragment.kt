package com.castcle.android.presentation.search

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.hideKeyboard
import com.castcle.android.databinding.FragmentSearchBinding
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.domain.search.type.SearchType.*
import com.castcle.android.presentation.search_result.SearchResultFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment() {

    private val viewModel by stateViewModel<SearchViewModel> { parametersOf(sessionId) }

    private val args by navArgs<SearchFragmentArgs>()

    private val sessionId = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateRecentSearch(args.keyword)
        viewModel.updateSearchKeyword(args.keyword)
    }

    override fun initViewProperties() {
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = SearchViewPagerAdapter(tabItemType)
        binding.viewPager.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) = Unit
            override fun onViewDetachedFromWindow(p0: View?) {
                binding.viewPager.adapter = null
                tabLayoutMediator.detach()
            }
        })
        tabLayoutMediator.attach()
        binding.searchBar.setText(args.keyword)
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.search,
        )
    }

    override fun initListener() {
        binding.searchBar.setSearchClickedListener {
            hideKeyboard()
            binding.searchBar.clearFocus()
            viewModel.updateRecentSearch(it)
            viewModel.updateSearchKeyword(it)
        }
    }

    private val tabItemType = listOf(Trend, Lastest, Photo, People)

    private val tabLayoutMediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            tab.text = tabItemType[index].id.replaceFirstChar { it.uppercaseChar() }
        }
    }

    private inner class SearchViewPagerAdapter(private val tabItemType: List<SearchType>) :
        FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {

        override fun getItemCount() = tabItemType.size

        override fun createFragment(position: Int): SearchResultFragment {
            return SearchResultFragment.newInstance(sessionId, tabItemType[position])
        }

    }

    private val binding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}