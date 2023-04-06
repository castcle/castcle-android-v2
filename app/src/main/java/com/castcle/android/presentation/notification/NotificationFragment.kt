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

package com.castcle.android.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateAppendAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateListener
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType
import com.castcle.android.core.custom_view.load_state.item_loading_state_notification.LoadingStateNotificationViewRenderer
import com.castcle.android.core.extensions.gone
import com.castcle.android.core.extensions.setRefreshColor
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.notification.item.NotificationViewRenderer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class NotificationFragment : BaseFragment(),
    CastcleListener,
    LoadStateListener,
    NotificationListener {

    private val directions = NotificationFragmentDirections

    private val viewModel by stateViewModel<NotificationViewModel>()

    @FlowPreview
    override fun initViewProperties() {
        binding.actionBar.gone()
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadStateAppendAdapter(compositeDisposable, this)
        )
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.notification,
        )
        viewLifecycleOwner.lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                pagingAdapter = adapter,
                pagingRecyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.NOTIFICATION,
            )
        }
    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitData)
        }
    }

    override fun onCastNotificationClicked(castId: String, notificationId: String) {
        viewModel.readNotification(notificationId = notificationId)
        directions.toContentFragment(castId, null).navigate()
    }

    override fun onFollowNotificationClicked(notificationId: String, userId: String) {
        viewModel.readNotification(notificationId = notificationId)
        directions.toFollowingFollowersFragment(false, userId).navigate()
    }

    override fun onOptionClicked(type: OptionDialogType) {
        directions.toOptionDialogFragment(type).navigate()
    }

    override fun onRefreshClicked() {
        adapter.refresh()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onStop() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onStop()
    }

    override fun onStart() {
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
        super.onStart()
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(NotificationViewRenderer())
            registerRenderer(LoadingStateNotificationViewRenderer(), true)
        }
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