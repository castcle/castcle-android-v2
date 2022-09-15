/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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

    override fun onChangePasswordClicked() {
        directions.toVerifyPasswordFragment().navigate()
    }

    override fun onDeleteAccountClicked() = Unit

    override fun onLinkFacebookClicked() {
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
                    viewModel.logoutFacebook()
                }

                override fun onError(error: FacebookException) {
                    viewModel.logoutFacebook()
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

    override fun onRegisterEmailClicked() {
        directions.toRegisterEmailFragment().navigate()
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
        savedInstanceState: Bundle?,
    ) = binding.root

}