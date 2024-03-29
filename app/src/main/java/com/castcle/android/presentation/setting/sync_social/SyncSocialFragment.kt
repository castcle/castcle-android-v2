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

package com.castcle.android.presentation.setting.sync_social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.string
import com.castcle.android.core.extensions.toast
import com.castcle.android.data.page.entity.SyncSocialRequest
import com.castcle.android.databinding.*
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.dialog.basic.BasicDialog
import com.castcle.android.presentation.setting.sync_social.dialog_duplicate_sync_social.DuplicateSyncSocialDialog
import com.castcle.android.presentation.setting.sync_social.item_sync_social.SyncSocialViewRenderer
import com.castcle.android.presentation.setting.view_facebook_page.ViewFacebookPageFragment.Companion.SELECT_FACEBOOK_PAGE
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SyncSocialFragment : BaseFragment(), SyncSocialListener {

    private val viewModel by viewModel<SyncSocialViewModel> { parametersOf(args.userId) }

    private val args by navArgs<SyncSocialFragmentArgs>()

    private val directions = SyncSocialFragmentDirections

    private val facebookLoginManager by inject<LoginManager>()

    private val callbackManager = CallbackManager.Factory.create()

    val twitterAuthClient by inject<TwitterAuthClient>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.fragment_sync_social_title_1,
        )
    }

    @Suppress("DEPRECATION")
    override fun initListener() {
        setFragmentResultListener(SELECT_FACEBOOK_PAGE) { _, bundle ->
            val page = bundle.getParcelable<SyncSocialRequest>(SELECT_FACEBOOK_PAGE)
            if (page != null) {
                showLoading()
                viewModel.syncWithFacebook(page)
            }
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest { (duplicate, syncSocial, user) ->
                dismissLoading()
                if (duplicate) {
                    DuplicateSyncSocialDialog(
                        binding = DialogDuplicateSyncSocialBinding.inflate(layoutInflater),
                        syncSocial = syncSocial,
                        user = user ?: UserEntity(),
                    ).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.onFacebookPageProfileResult.collectLatest {
                dismissLoading()
                directions.toViewFacebookPageFragment(it.toTypedArray()).navigate()
            }
        }
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                BasicDialog(
                    binding = DialogBasicBinding.inflate(layoutInflater),
                    button = string(R.string.ok),
                    isCancelable = false,
                    title = it.message.orEmpty(),
                ).show()
            }
        }
    }

    override fun onSyncSocialClicked(syncSocial: SyncSocialEntity?) {
        directions.toSyncSocialDetailFragment(syncSocial?.id.orEmpty()).navigate()
    }

    override fun onSyncWithFacebookClicked() {
        facebookLoginManager.logOut()
        facebookLoginManager.logInWithReadPermissions(
            callbackManager = callbackManager,
            fragment = this,
            permissions = listOf("pages_manage_metadata", "pages_show_list"),
        )
        facebookLoginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    showLoading()
                    viewModel.getFacebookPageProfile()
                }

                override fun onCancel() {
                    viewModel.logoutFacebook()
                }

                override fun onError(error: FacebookException) {
                    toast(error.message)
                    viewModel.logoutFacebook()
                }
            })
    }

    override fun onSyncWithTwitterClicked() {
        twitterAuthClient.cancelAuthorize()
        twitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
            override fun failure(exception: TwitterException?) = toast(exception?.message)
            override fun success(result: Result<TwitterSession>?) {
                showLoading()
                viewModel.syncWithTwitter(result?.data?.authToken)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.trackViewSyncSocial()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(SyncSocialViewRenderer())
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