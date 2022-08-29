package com.castcle.android.presentation.setting.request_otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.metadata.entity.CountryCodeEntity
import com.castcle.android.presentation.setting.country_code.CountryCodeFragment.Companion.SELECT_COUNTRY_CODE
import com.castcle.android.presentation.setting.request_otp.item_request_otp_email.RequestOtpEmailViewRenderer
import com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile.RequestOtpMobileViewEntity
import com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile.RequestOtpMobileViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RequestOtpFragment : BaseFragment(), RequestOtpListener {

    private val viewModel by viewModel<RequestOtpViewModel> { parametersOf(args.type) }

    private val args by navArgs<RequestOtpFragmentArgs>()

    private val directions = RequestOtpFragmentDirections

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = when (args.type) {
                is OtpType.Email, is OtpType.Password -> R.string.password
                is OtpType.Mobile -> R.string.mobile_number
            },
        )
    }

    override fun initListener() {
        setFragmentResultListener(SELECT_COUNTRY_CODE) { _, bundle ->
            val countryCode = bundle.getParcelable<CountryCodeEntity>(SELECT_COUNTRY_CODE)
            val items = viewModel.views.value
                ?.cast<RequestOtpMobileViewEntity>()
                ?.let { it.copy(countryCode = countryCode ?: it.countryCode) }
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

    override fun onMobileCountryCodeClicked() {
        directions.toCountryCodeFragment().navigate()
    }

    override fun onRequestOtp(countryCode: String, email: String, mobileNumber: String) {
        showLoading()
        hideKeyboard()
        viewModel.requestOtp(
            countryCode = countryCode,
            email = email,
            mobileNumber = mobileNumber,
        )
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
            registerRenderer(RequestOtpEmailViewRenderer())
            registerRenderer(RequestOtpMobileViewRenderer())
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