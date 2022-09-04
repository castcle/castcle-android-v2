package com.castcle.android.presentation.wallet.wallet_dashboard

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType
import com.castcle.android.core.custom_view.load_state.item_error_append.ErrorAppendViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_append.LoadingAppendViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance.WalletDashboardBalanceViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty.WalletDashboardEmptyViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history.WalletDashboardHistoryViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogFragment.Companion.SELECT_FILTER
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogFragment.Companion.SELECT_USER
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletDashboardFragment : BaseFragment(), WalletDashboardListener {

    private val viewModel by viewModel<WalletDashboardViewModel>()

    private val directions = WalletDashboardFragmentDirections

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            rightButtonAction = {
                val userId = viewModel.userId.value.orEmpty()
                val requestType = WalletScanQrCodeRequestType.FromDashboard(userId)
                directions.toWalletScanQrCodeFragment(requestType).navigate()
            },
            rightButtonIcon = R.drawable.ic_qr_code,
            title = R.string.wallet,
        )
    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.refreshData()
        }
        setFragmentResultListener(SELECT_FILTER) { _, bundle ->
            bundle.getParcelable<Parcelable>(SELECT_FILTER)
                ?.cast<WalletHistoryFilter>()
                ?.let { viewModel.filter.value = it }
        }
        setFragmentResultListener(SELECT_USER) { _, bundle ->
            bundle.getString(SELECT_USER)?.let { viewModel.userId.value = it }
        }
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                loadState = viewModel.loadState,
                recyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.DEFAULT,
            )
        }
    }

    override fun onAirdropClicked() {
        viewModel.getAccessToken {
            openUrl(getString(R.string.airdrop_url, it))
        }
    }

    override fun onDepositClicked(currentUserId: String) {
        directions.toWalletDepositFragment(currentUserId).navigate()
    }

    override fun onFilterClicked(currentFilter: WalletHistoryFilter) {
        directions.toWalletDashboardDialogFragment(currentFilter, null).navigate()
    }

    override fun onSelectUserClicked(currentUserId: String) {
        directions.toWalletDashboardDialogFragment(null, currentUserId).navigate()
    }

    override fun onSendClicked(currentUserId: String) {
        directions.toWalletSendFragment(
            targetCastcleId = null,
            targetUserId = null,
            userId = currentUserId,
        ).navigate()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorAppendViewRenderer())
            registerRenderer(LoadingAppendViewRenderer())
            registerRenderer(WalletDashboardBalanceViewRenderer())
            registerRenderer(WalletDashboardEmptyViewRenderer())
            registerRenderer(WalletDashboardHistoryViewRenderer())
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