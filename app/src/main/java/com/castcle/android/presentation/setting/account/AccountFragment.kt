package com.castcle.android.presentation.setting.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.presentation.setting.account.item_menu.AccountMenuViewRenderer
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewRenderer
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class AccountFragment : BaseFragment(), AccountListener {

    private val viewModel by stateViewModel<AccountViewModel>()

    private val facebookLoginManager by inject<LoginManager>()

    private val callbackManager = CallbackManager.Factory.create()

    val twitterAuthClient by inject<TwitterAuthClient>()

    private val directions = AccountFragmentDirections

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.account,
        )
    }

    override fun initListener() {
        viewModel.onError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
    }

    override fun onChangePasswordClicked() = Unit

    override fun onDeleteAccountClicked() = Unit

    override fun onLinkFacebookClicked() {
        facebookLoginManager.logOut()
        facebookLoginManager.logInWithReadPermissions(
            callbackManager = callbackManager,
            fragment = this,
            permissions = listOf("email", "public_profile"),
        )
        facebookLoginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    viewModel.linkWithFacebook()
                    showLoading()
                }

                override fun onCancel() {
                    facebookLoginManager.logOut()
                }

                override fun onError(error: FacebookException) {
                    facebookLoginManager.logOut()
                    toast(error.message)
                }
            })
    }

    override fun onLinkTwitterClicked() {
        twitterAuthClient.cancelAuthorize()
        twitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
            override fun failure(exception: TwitterException?) = toast(exception?.message)
            override fun success(result: Result<TwitterSession>?) {
                viewModel.linkWithTwitter(result?.data?.authToken)
                showLoading()
            }
        })
    }

    override fun onRequestOtpClicked(type: OtpType) {
        directions.toRequestOtpFragment(type).navigate()
    }

    override fun onResentVerifyEmailClicked() {
        directions.toResentVerifyEmailFragment().navigate()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(AccountMenuViewRenderer())
            registerRenderer(AccountTitleViewRenderer())
            registerRenderer(LoadingViewRenderer())
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