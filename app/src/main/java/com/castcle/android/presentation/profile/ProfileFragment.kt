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

package com.castcle.android.presentation.profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ConcatAdapter
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.*
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.core.work.StateWorkLoading
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.ads.type.BoostAdBundle
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewRenderer
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewRenderer
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewRenderer
import com.castcle.android.presentation.feed.item_feed_report.FeedReportViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewRenderer
import com.castcle.android.presentation.home.HomeViewModel
import com.castcle.android.presentation.profile.item_profile_page.ProfilePageViewRenderer
import com.castcle.android.presentation.profile.item_profile_user.ProfileUserViewRenderer
import com.castcle.android.presentation.sign_up.entity.ProfileBundle
import com.castcle.android.presentation.sign_up.update_profile.UpdateProfileFragment
import com.castcle.android.presentation.sign_up.update_profile.entity.PhotoSelectedState
import com.stfalcon.imageviewer.StfalconImageViewer
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class ProfileFragment : BaseFragment(), LoadStateListener, FeedListener, ProfileListener {

    private val viewModel by stateViewModel<ProfileViewModel> { parametersOf(args.user.id) }

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    private val directions = ProfileFragmentDirections

    private val args by navArgs<ProfileFragmentArgs>()

    private val launcherPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let {
                    when (viewModel.selectState.value) {
                        PhotoSelectedState.AVATAR_SELECT ->
                            lifecycleScope.launch {
                                viewModel.imageAvatarUri.value = it
                            }
                        PhotoSelectedState.COVER_SELECT ->
                            lifecycleScope.launch {
                                viewModel.imageCoverUri.value = it
                            }
                        else -> Unit
                    }
                }
            }
        }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
        ConcatAdapter(
            config, adapterUser, adapter.withLoadStateFooter(
                footer = LoadStateAppendAdapter(compositeDisposable, this)
            )
        )
    }

    @FlowPreview
    override fun initViewProperties() {
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = concatAdapter
    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.fetchUser()
            adapter.refresh()
        }

        setFragmentResultListener(UpdateProfileFragment.OPTIONAL_SELECT) { key, bundle ->
            if (key == UpdateProfileFragment.OPTIONAL_SELECT) {
                val result = bundle.getInt(UpdateProfileFragment.OPTIONAL_SELECT)
                when (viewModel.selectState.value) {
                    PhotoSelectedState.AVATAR_SELECT ->
                        requireActivity().getIntentImagePicker(result, true)
                    PhotoSelectedState.COVER_SELECT ->
                        requireActivity().getIntentImagePicker(result, false)
                    else -> null
                }.let {
                    it?.run {
                        onRequestPermission(onGrantPass = {
                            launcherPicker.launch(it)
                        })
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitData)
        }
        lifecycleScope.launch {
            viewModel.currentUser.collectLatest {
                binding.actionBar.bind(
                    leftButtonAction = { backPress() },
                    title = it?.displayName ?: args.user.displayName,
                )
            }
        }
        lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                loadState = viewModel.loadState,
                pagingAdapter = adapter,
                pagingRecyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.PROFILE,
            )
        }

        lifecycleScope.launch {
            viewModel.userItemView.collectLatest {
                it?.let { it1 ->
                    adapterUser.submitList(it1)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.pageItemView.collectLatest {
                it?.let { it1 ->
                    adapterUser.submitList(it1)
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.checkWorkUpLoadImage()?.subscribe {
            if (it == StateWorkLoading.SUCCESS || it == StateWorkLoading.ERROR) {
                val message = if (it == StateWorkLoading.SUCCESS) {
                    requireContext().getString(R.string.upload_image_success)
                } else {
                    requireContext().getString(R.string.upload_image_failed)
                }
                viewModel.onStopLoadingImage()
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }?.addTo(compositeDisposable)
    }

    override fun onCommentClicked(cast: CastEntity, user: UserEntity) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toContentFragment(cast.id, user.displayName).navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
        )
    }

    override fun onFollowClicked(user: UserEntity) {
        shareViewModel.followUser(
            isGuestAction = { directions.toLoginFragment().navigate() },
            targetUser = user,
        )
    }

    override fun onEditProfileClicked(user: UserEntity) {
        when (user.type) {
            is UserType.People -> {
                navigateToEditProfileFragment(
                    profileBundle = ProfileBundle.User(
                        userId = user.id,
                        displayName = user.displayName
                    )
                )
            }
            is UserType.Page -> {
                navigateToEditProfileFragment(
                    profileBundle = ProfileBundle.Page(
                        userId = user.id,
                        displayName = user.displayName
                    )
                )
            }
        }
    }

    private fun navigateToEditProfileFragment(profileBundle: ProfileBundle) {
        directions.toEditProfileFragment(profileBundle).navigate()
    }

    override fun onFollowingFollowersClicked(isFollowing: Boolean, user: UserEntity) {
        directions.toFollowingFollowersFragment(isFollowing, user.id).navigate()
    }

    override fun onHashtagClicked(keyword: String) {
        directions.toSearchFragment(keyword).navigate()
    }

    override fun onAddAvatarClick() {
        viewModel.selectState.value = PhotoSelectedState.AVATAR_SELECT
        handlerOptionalSelect()
    }

    override fun onAddCoverClick() {
        viewModel.selectState.value = PhotoSelectedState.COVER_SELECT
        handlerOptionalSelect()
    }

    private fun handlerOptionalSelect() {
        directions.toOptionDialogFragment(OptionDialogType.CameraOption).navigate()
    }

    override fun onImageClicked(image: List<ImageEntity>, position: Int) {
        StfalconImageViewer.Builder(context, image, ::loadViewLargeImage)
            .withStartPosition(position)
            .withHiddenStatusBar(true)
            .allowSwipeToDismiss(true)
            .allowZooming(true)
            .show()
    }

    override fun onLikeClicked(cast: CastEntity) {
        shareViewModel.likeCast(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
            targetCast = cast,
        )
    }

    override fun onLinkClicked(url: String) {
        openUrl(url)
    }

    override fun onMentionClicked(castcleId: String) {
        directions.toProfileFragment(UserEntity(id = castcleId)).navigate()
    }

    override fun onNewCastClicked(userId: String) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toNewCastFragment(null, userId).navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
        )
    }

    override fun onOptionClicked(type: OptionDialogType) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toOptionDialogFragment(type).navigate() },
        )
    }

    override fun onRecastClicked(cast: CastEntity) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toRecastDialogFragment(contentId = cast.id).navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
        )
    }

    override fun onRefreshClicked() {
        adapter.refresh()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onUserClicked(user: UserEntity) {
        directions.toProfileFragment(user).navigate()
    }

    override fun onViewReportClicked(id: String, ignoreReportContentId: List<String>) {
        viewModel.showReportingContent(id = id, ignoreReportContentId = ignoreReportContentId)
    }

    override fun onBoostCastClicked(cast: CastEntity) {
        directions.toBoostAdsFragment(
            BoostAdBundle.BoostAdContentBundle(cast.id, cast.authorId)
        ).navigate()
    }

    private fun onRequestPermission(onGrantPass: () -> Unit) {
        checkPermissionCamera(onGrant = {
            onGrantPass.invoke()
        })
    }

    override fun onStop() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchUser()
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(FeedImageViewRenderer())
            registerRenderer(FeedNewCastViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedReportViewRenderer())
            registerRenderer(FeedTextViewRenderer())
            registerRenderer(FeedWebViewRenderer())
            registerRenderer(FeedWebImageViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer(), isDefaultItem = true)
        }
    }

    private val adapterUser by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ProfilePageViewRenderer())
            registerRenderer(ProfileUserViewRenderer())
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