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

package com.castcle.android.presentation.new_cast

import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.mention_view.MentionView
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentNewCastBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_item.FeedImageItemViewRenderer
import com.castcle.android.presentation.feed.item_feed_report.FeedReportViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewRenderer
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class NewCastFragment : BaseFragment(), FeedListener, FeedImageViewListener {

    private val viewModel by stateViewModel<NewCastViewModel> { parametersOf(args.userId) }

    private val args by navArgs<NewCastFragmentArgs>()

    override fun initViewProperties() {
        updateCastButton()
        updateMessageCount()
        binding.ivSelectedImage.isVisible = args.quoteCastId == null
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (position) {
                    0, 2 -> if (adapter.itemCount == position + 1) 2 else 1
                    else -> 1
                }
            }
        }
        binding.viewContent.layoutParams.height = getScreenHeight(requireActivity())
            .minus(dimenPx(com.intuit.sdp.R.dimen._84sdp))
            .minus(getStatusBarHeight(requireActivity()))
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = if (args.quoteCastId != null) {
                R.string.quote_cast
            } else {
                R.string.new_cast
            },
        )
    }

    override fun initListener() {
        binding.etMessage.setMentionEnabled(true)
        binding.etMessage.setMentionTextChangedListener(object : MentionView.OnChangedListener {
            override fun onChanged(view: MentionView, text: CharSequence) {
                viewModel.getMentions(text.toString())
            }
        })
        binding.etMessage.setHashtagEnabled(true)
        binding.etMessage.setHashtagTextChangedListener(object : MentionView.OnChangedListener {
            override fun onChanged(view: MentionView, text: CharSequence) {
                viewModel.getHashtag(text.toString())
            }
        })
        compositeDisposable += binding.etMessage.onTextChange {
            updateMessageCount()
            updateCastButton()
        }
        compositeDisposable += binding.viewContent.onClick {
            binding.etMessage.showKeyboard()
        }
        compositeDisposable += binding.ivSelectedImage.onClick {
            selectImageCallback.launch("image/*")
        }
        compositeDisposable += binding.tvCast.onClick {
            showLoading()
            hideKeyboard()
            if (args.quoteCastId != null) {
                viewModel.createQuoteCast(
                    contentId = args.quoteCastId.orEmpty(),
                    message = binding.etMessage.text.toString(),
                )
            } else {
                viewModel.createCast(
                    context = requireContext(),
                    message = binding.etMessage.text.toString(),
                )
            }
        }
    }

    override fun initObserver() {
        viewModel.getQuoteCast(args.quoteCastId)
        viewModel.createContentError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.createContentSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
        viewModel.mentions.observe(viewLifecycleOwner) {
            binding.etMessage.updateMentionsItems(it)
        }
        viewModel.hashtags.observe(viewLifecycleOwner) {
            binding.etMessage.updateHashtagItems(it)
        }
    }

    private fun updateMessageCount() {
        val messageCount = 280.minus(binding.etMessage.text.length)
        val color = if (messageCount < 0) color(R.color.red_3) else color(R.color.white)
        binding.tvMessageCount.text = messageCount.toString()
        binding.tvMessageCount.setTextColor(color)
    }

    private val selectImageCallback = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        if (it.isNotEmpty()) {
            viewModel.imageUri.value = it.take(4)
        }
    }

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.currentUser.filterNotNull().collectLatest {
                binding.ivAvatar.loadAvatarImage(it.avatar.thumbnail)
                binding.tvDisplayName.text = it.displayName
            }
        }
        lifecycleScope.launch {
            flowOf(viewModel.imageItems, viewModel.quoteCast)
                .flattenMerge()
                .collectLatest {
                    adapter.submitList(it.orEmpty())
                    updateCastButton()
                }
        }
    }

    private fun updateCastButton() {
        val checkImageIsNotEmpty = viewModel.imageUri.value.isNotEmpty()
        val checkMessageIsNotEmpty = binding.etMessage.text.toString().length in 1..280
        val checkMessageLowerThanMaximum = binding.etMessage.text.toString().length <= 280
        val isCanCast = checkImageIsNotEmpty
            .and(checkMessageLowerThanMaximum)
            .or(checkMessageIsNotEmpty)
        binding.tvCast.isEnabled = isCanCast
        binding.tvCast.backgroundTintList = if (isCanCast) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.black_background_1)
        }
        binding.tvCast.foregroundTintList = if (isCanCast) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.black)
        }
        binding.tvCast.setTextColor(
            if (isCanCast) {
                color(R.color.white)
            } else {
                color(R.color.gray_5)
            }
        )
        binding.ivSelectedImage.imageTintList = if (checkImageIsNotEmpty) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.white)
        }
    }

    override fun onChildImageDeleteClicked(position: Int) {
        viewModel.imageUri.value = viewModel.imageUri.value
            .toMutableList()
            .apply { removeAt(position) }
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
            registerRenderer(FeedImageViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedImageItemViewRenderer())
            registerRenderer(FeedReportViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedTextViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedWebViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedWebImageViewRenderer(FeedDisplayType.NewCast))
        }
    }

    private val binding by lazy {
        FragmentNewCastBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}