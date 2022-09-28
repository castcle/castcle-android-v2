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

package com.castcle.android.presentation.content.content_metrics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.extensions.setRefreshColor
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.content.content_metrics.item_content_metrics_dialog.ContentMetricsDialogViewRenderer
import com.castcle.android.presentation.content.content_metrics_dialog.ContentMetricsDialogFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ContentMetricsFragment : BaseFragment(), ContentMetricsListener {

    private val viewModel by viewModel<ContentMetricsViewModel> { parametersOf(type) }

    private val directions = ContentMetricsDialogFragmentDirections

    override fun initViewProperties() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = null
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitData)
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }
    }

    override fun onUserClicked(user: UserEntity) {
        directions.toProfileFragment(user = user).navigate()
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(ContentMetricsDialogViewRenderer())
        }
    }

    @Suppress("DEPRECATION")
    private val type by lazy {
        arguments?.getParcelable<ContentMetricsType>(CONTENT_METRICS_TYPE)
            ?: ContentMetricsType.Like(contentId = UUID.randomUUID().toString())
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = binding.root

    companion object {
        private const val CONTENT_METRICS_TYPE = "CONTENT_METRICS_TYPE"
        fun newInstance(type: ContentMetricsType) = ContentMetricsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CONTENT_METRICS_TYPE, type)
            }
        }
    }

}