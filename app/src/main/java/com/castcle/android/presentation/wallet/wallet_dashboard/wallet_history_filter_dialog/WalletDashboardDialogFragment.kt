package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog.WalletDashboardDialogFilterViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog.WalletDashboardDialogUserViewRenderer
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletDashboardDialogFragment : BaseBottomSheetDialogFragment(),
    WalletDashboardDialogListener {

    private val viewModel by viewModel<WalletDashboardDialogViewModel>()

    private val args by navArgs<WalletDashboardDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initObserver() {
        viewModel.getItems(args.currentFilter, args.currentUserId)
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onFilterClicked(filter: WalletHistoryFilter) {
        setFragmentResult(SELECT_FILTER, bundleOf(SELECT_FILTER to filter))
        backPress()
    }

    override fun onUserClicked(user: UserEntity) {
        setFragmentResult(SELECT_USER, bundleOf(SELECT_USER to user.id))
        backPress()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletDashboardDialogFilterViewRenderer())
            registerRenderer(WalletDashboardDialogUserViewRenderer())
        }
    }

    private val binding by lazy {
        DialogOptionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    companion object {
        const val SELECT_FILTER = "SELECT_FILTER"
        const val SELECT_USER = "SELECT_USER"
    }

}