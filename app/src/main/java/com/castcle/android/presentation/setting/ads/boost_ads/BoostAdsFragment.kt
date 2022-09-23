package com.castcle.android.presentation.setting.ads.boost_ads

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.error.CastcleException
import com.castcle.android.core.extensions.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.databinding.LayoutRecyclerStaticViewBinding
import com.castcle.android.domain.ads.type.*
import com.castcle.android.presentation.dialog.warning.CommonDialogFragment
import com.castcle.android.presentation.dialog.warning.entity.CommonWarningBase
import com.castcle.android.presentation.setting.ads.ads_manage.AdsManageFragment
import com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_ad_content.ItemAdContentViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_ad_page.ItemPreviewAdPageViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_error.ItemErrorViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.item_budget.ItemBudgetViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.item_campaign.ItemCampaignViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_campaign.ItemCampaignViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_objective.ItemChooseObjectiveViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_page_boost.ItemChoosePageViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_payment.ItemChoosePaymentViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.PageChooseFragment.Companion.SELECT_CHOOSE_OBJECTIVE
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.PageChooseFragment.Companion.SELECT_CHOOSE_PAGE
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.PageChooseFragment.Companion.SELECT_CHOOSE_PAYMENT
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 14/9/2022 AD at 15:05.

class BoostAdsFragment : BaseFragment(), BoostAdsListener {

    private val viewModel by stateViewModel<BoostAdsViewModel>()

    private val args by navArgs<BoostAdsFragmentArgs>()

    private val directions = BoostAdsFragmentDirections

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ItemChoosePageViewRenderer())
            registerRenderer(ItemChooseObjectiveViewRenderer())
            registerRenderer(ItemCampaignViewRenderer())
            registerRenderer(ItemBudgetViewRenderer())
            registerRenderer(ItemChoosePaymentViewRenderer())
            registerRenderer(ItemPreviewAdPageViewRenderer())
            registerRenderer(ItemAdContentViewRenderer())
            registerRenderer(ItemErrorViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerStaticViewBinding.inflate(layoutInflater)
    }

    private var onFocusDaily = false

    private val keyboardListener = object : KeyboardListener() {

        @SuppressLint("SetTextI18n")
        override fun onVisibilityStateChanged(isShown: Boolean) {
            val delay = if (isShown) HIDE_DELAY_IN_MILLISECONDS else SHOW_DELAY_IN_MILLISECONDS
            handlerBottomContent(isShown, delay)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onPause() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onPause()
    }

    override fun onResume() {
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
        super.onResume()
    }

    override fun initConsumer() {
        when (args.boostBundle) {
            is BoostAdBundle.BoostAdPageBundle -> {
                viewModel.boostType.value = AdvertiseType.User
            }
            is BoostAdBundle.BoostAdContentBundle -> {
                viewModel.boostType.value = AdvertiseType.Content
                viewModel.castId.value =
                    (args.boostBundle as BoostAdBundle.BoostAdContentBundle).castId
                viewModel.userChange.value =
                    (args.boostBundle as BoostAdBundle.BoostAdContentBundle).userId
            }
            else -> Unit
        }

        lifecycleScope.launch {
            viewModel.statePreview.collectLatest {
                if (it) {
                    handlerBoostPreview()
                } else {
                    handlerBoostAdView()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                handlerUiState(it)
            }
        }
    }

    private fun handlerUiState(baseUiState: BaseUiState<Nothing>) {
        when (baseUiState) {
            is BaseUiState.SuccessNonBody -> {
                setFragmentResult(
                    AdsManageFragment.BOOST_SUCCESS,
                    bundleOf(AdsManageFragment.BOOST_SUCCESS to true)
                )
                backPress()
            }
            is BaseUiState.Loading -> {
                if (baseUiState.isLoading) {
                    showLoading()
                } else {
                    dismissLoading()
                }
            }
            is BaseUiState.Error -> {
                if (baseUiState.exception is CastcleException.BoostAdException) {
                    viewModel.errorBoostAds.value = baseUiState.exception.messageList
                } else {
                    CommonDialogFragment.newInstance(
                        CommonWarningBase.WarningUiModel(
                            titleWarning = binding.root.context.getString(
                                R.string.dialog_common_warning
                            ),
                            warningDescription = baseUiState.exception.message ?: ""
                        )
                    ).show(childFragmentManager, "")
                }
            }
            else -> Unit
        }
    }

    private fun handlerBoostPreview() {
        onBoostType(onBoostPage = {
            onBindButtonTitle(string(R.string.ad_fragment_boost_page))
        }, onBoostContent = {
            onBindButtonTitle(string(R.string.ad_fragment_boost_cast))
        })
        onBindTitleToolBar(string(R.string.ad_fragment_ap_preview))
    }

    private fun handlerBoostAdView() {
        onBoostType(onBoostPage = {
            onBindTitleToolBar(string(R.string.ad_boost_page))
        }, onBoostContent = {
            onBindTitleToolBar(string(R.string.ad_boost_cast))
        })
        onBindButtonTitle(string(R.string.ad_fragment_ap_preview))
    }

    private fun onBoostType(onBoostPage: () -> Unit, onBoostContent: () -> Unit) {
        if (viewModel.boostType.value == AdvertiseType.User) {
            onBoostPage.invoke()
        } else {
            onBoostContent.invoke()
        }
    }

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        var titleMessage = 0
        when (viewModel.boostType.value) {
            AdvertiseType.User -> {
                titleMessage = R.string.ad_boost_page
            }
            AdvertiseType.Content -> {
                titleMessage = R.string.ad_boost_cast
            }
            else -> Unit
        }
        onBindTitleToolBar(string(titleMessage))

        compositeDisposable += binding.btDone.onClick {
            requireActivity().hideKeyboard()
        }

        compositeDisposable += binding.btBoostPage.onClick {
            viewModel.onPreViewAdPage()
        }

        addOnBackPressedCallback {
            if (viewModel.statePreview.value) {
                viewModel.onBoostAdView()
            } else {
                backPress()
            }
        }
    }

    private fun onBindTitleToolBar(title: String) {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = title,
        )
    }

    private fun onBindButtonTitle(title: String) {
        binding.btBoostPage.text = title
    }

    override fun initObserver() {
        viewModel.viewItem.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onLimitDailyBudget() {
        CommonDialogFragment.newInstance(
            CommonWarningBase.WarningUiModel(
                titleWarning = binding.root.context.getString(R.string.dialog_common_warning),
                warningDescription = binding.root.context.getString(R.string.ads_daily_budget_warning)
            )
        ).show(childFragmentManager, "")
    }

    override fun initListener() {
        setFragmentResultListener(SELECT_CHOOSE_PAGE) { _, bundle ->
            bundle.getString(SELECT_CHOOSE_PAGE).let {
                viewModel.userChange.value = it
            }
        }

        setFragmentResultListener(SELECT_CHOOSE_OBJECTIVE) { _, bundle ->
            bundle.getParcelable<ObjectiveType>(SELECT_CHOOSE_OBJECTIVE).let {
                viewModel.objectiveChange.value = it
            }
        }

        setFragmentResultListener(SELECT_CHOOSE_PAYMENT) { _, bundle ->
            bundle.getParcelable<PaymentType>(SELECT_CHOOSE_PAYMENT).let {
                viewModel.paymentChange.value = it
            }
        }
    }

    override fun onChoosePageClick(userId: String) {
        handlerNavigateToPageChooseFragment(BoostAdBundle.BoostChoosePageBundle(userId))
    }

    override fun onChooseObjective(objective: ObjectiveType) {
        handlerNavigateToPageChooseFragment(BoostAdBundle.BoostChooseObjectiveBundle(objective))
    }

    override fun onChoosePayment(userId: String, payment: PaymentType) {
        handlerNavigateToPageChooseFragment(BoostAdBundle.BoostChoosePaymentBundle(userId, payment))
    }

    override fun onLearnMoreClick() {

    }

    private fun handlerNavigateToPageChooseFragment(boostAdBundle: BoostAdBundle) {
        directions.toPageChooseFragment(boostAdBundle).navigate()
    }

    override fun onFocusDaily() {
        onFocusDaily = true
    }

    override fun onShowKeyboard(view: View) {
        requireActivity().showSoftKeyboard()
    }

    override fun onHideKeyboard(view: View) {
        onFocusDaily = false
        requireActivity().hideKeyboardOnView(view)
    }

    override fun onInputCampaignName(message: String) {
        bindBannerCount(campaignName = message.length)
    }

    override fun onInputCampaignMessage(name: String, message: String) {
        bindBannerCount(campaignName = name.length, campaignMessage = message.length)
    }

    private fun bindBannerCount(
        campaignName: Int? = null,
        campaignMessage: Int? = null
    ) {
        val count = when {
            campaignName != null -> {
                LIMIT_CAMPAIGN_NAME - campaignName
            }
            campaignName == 0 -> {
                LIMIT_CAMPAIGN_NAME
            }
            campaignMessage != null -> {
                LIMIT_CAMPAIGN_MESSAGE - campaignMessage
            }
            campaignMessage == 0 -> {
                LIMIT_CAMPAIGN_MESSAGE
            }
            else -> 0
        }
        val colorCount = when {
            count < 0 -> R.color.red_3
            else -> R.color.white
        }

        with(binding) {
            tvCount.run {
                setTextColor(requireContext().getColor(colorCount))
                text = count.toString()
            }
            btDone.run {
                setStateDone(count >= 0)
                isEnabled = count >= 0
            }
        }
        onEnableBoost(
            ((campaignName ?: 0) != 0 && count >= 0) || (campaignMessage ?: 0) != 0 && count >= 0
        )
    }

    override fun onCampaignNameFocus() {
        val campaignName =
            viewModel.viewItem.value?.filterIsInstance<ItemCampaignViewEntity>()?.let {
                it.firstOrNull()?.campaignName
            }
        bindBannerCount(campaignName = campaignName?.length ?: 0)
    }

    override fun onCampaignMessageFocus() {
        val campaignMessage =
            viewModel.viewItem.value?.filterIsInstance<ItemCampaignViewEntity>()?.let {
                it.firstOrNull()?.campaignMessage
            }
        bindBannerCount(campaignMessage = campaignMessage?.length ?: 0)
    }

    override fun onEnableBoost(isPass: Boolean) {
        binding.btBoostPage.run {
            setStatePass(isPass)
            isEnabled = isPass
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerKeyboardListener(keyboardListener)
        changeSoftInputMode(true)
    }

    override fun onStop() {
        requireActivity().unregisterKeyboardListener(keyboardListener)
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.initDefaultValue()
        super.onDestroy()
    }

    private fun handlerBottomContent(shown: Boolean, delay: Long) {
        with(binding) {
            clToolBarCast.visibleOrGone(shown && !onFocusDaily, delay)
            btBoostPage.visibleOrGone(!shown)
        }
    }

    override fun onBoostStatusClick(status: AdBoostStatusType) = Unit

    override fun onShowCancel(isShow: Boolean) = Unit
}

private const val LIMIT_CAMPAIGN_MESSAGE = 280
private const val LIMIT_CAMPAIGN_NAME = 50