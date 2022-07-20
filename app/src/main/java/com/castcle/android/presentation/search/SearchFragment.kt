package com.castcle.android.presentation.search

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class SearchFragment : BaseFragment() {

    private val viewModel by stateViewModel<SearchViewModel>()

    private val args by navArgs<SearchFragmentArgs>()

    override fun initViewProperties() {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = args.keyword,
        )
    }

    override fun initListener() {

    }

    override fun initObserver() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateRecentSearch(args.keyword)
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}