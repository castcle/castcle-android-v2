package com.castcle.android.presentation.setting.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.setting.forgot_password.item_forgot_password.ForgotPasswordViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : BaseFragment(), ForgotPasswordListener {

    private val viewModel by viewModel<ForgotPasswordViewModel>()

    private val directions = ForgotPasswordFragmentDirections

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
        )
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                toast(it.message)
            }
        }
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest {
                dismissLoading()
                directions.toVerifyOtpFragment(it).navigate()
            }
        }
    }


    override fun onForgotPasswordClicked(email: String) {
        showLoading()
        hideKeyboard()
        viewModel.forgotPassword(email)
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
            registerRenderer(ForgotPasswordViewRenderer())
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