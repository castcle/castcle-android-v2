package com.castcle.android.presentation.setting.register_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.metadata.entity.CountryCodeEntity
import com.castcle.android.presentation.setting.country_code.CountryCodeFragment.Companion.SELECT_COUNTRY_CODE
import com.castcle.android.presentation.setting.register_mobile.item_register_mobile.RegisterMobileViewEntity
import com.castcle.android.presentation.setting.register_mobile.item_register_mobile.RegisterMobileViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterMobileFragment : BaseFragment(), RegisterMobileListener {

    private val viewModel by viewModel<RegisterMobileViewModel>()

    private val directions = RegisterMobileFragmentDirections

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.mobile_number,
        )
    }

    override fun initListener() {
        setFragmentResultListener(SELECT_COUNTRY_CODE) { _, bundle ->
            val countryCode = bundle.getParcelable<CountryCodeEntity>(SELECT_COUNTRY_CODE)
            val items = viewModel.views.value
                ?.map { it.copy(countryCode = countryCode ?: it.countryCode) }
                ?: listOf(RegisterMobileViewEntity())
            viewModel.views.postValue(items)
        }
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest {
                dismissLoading()
                directions.toVerifyOtpFragment(it).navigate()
            }
        }
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                toast(it.message)
            }
        }
    }

    override fun onConfirmClicked(countryCode: CountryCodeEntity, mobileNumber: String) {
        showLoading()
        hideKeyboard()
        viewModel.requestOtpMobile(countryCode.dialCode, mobileNumber)
    }

    override fun onMobileCountryCodeClicked() {
        directions.toCountryCodeFragment().navigate()
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
            registerRenderer(RegisterMobileViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}