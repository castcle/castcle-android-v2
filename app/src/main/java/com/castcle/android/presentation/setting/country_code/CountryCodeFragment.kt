package com.castcle.android.presentation.setting.country_code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewWithSearchBinding
import com.castcle.android.domain.metadata.entity.CountryCodeEntity
import com.castcle.android.presentation.setting.country_code.item_country_code.CountryCodeViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountryCodeFragment : BaseFragment(), CountryCodeListener {

    private val viewModel by viewModel<CountryCodeViewModel>()

    override fun initViewProperties() {
        binding.searchBar.setHintText(string(R.string.search))
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.fragment_country_code_title_1,
        )
    }

    override fun initListener() {
        binding.searchBar.setTextChangeWithoutDebounceListener { keyword ->
            viewModel.searchKeyword.value = keyword
        }
        binding.searchBar.setSearchClickedListener {
            hideKeyboard()
            binding.searchBar.clearFocus()
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
    }

    override fun onCountryCodeClicked(countryCode: CountryCodeEntity) {
        setFragmentResult(SELECT_COUNTRY_CODE, bundleOf(SELECT_COUNTRY_CODE to countryCode))
        backPress()
    }

    override fun onStop() {
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        changeSoftInputMode(true)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(CountryCodeViewRenderer())
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewWithSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    companion object {
        const val SELECT_COUNTRY_CODE = "SELECT_COUNTRY_CODE"
    }

}