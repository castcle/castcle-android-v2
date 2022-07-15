package com.castcle.android.presentation.search

import android.os.Bundle
import android.view.*
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment() {

    private val binding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}