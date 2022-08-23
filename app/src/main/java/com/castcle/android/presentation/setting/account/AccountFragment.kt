package com.castcle.android.presentation.setting.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.setting.account.item_menu.AccountMenuViewRenderer
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewEntity
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class AccountFragment : BaseFragment(), AccountListener {

    private val viewModel by stateViewModel<AccountViewModel>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.account,
        )
        adapter.submitList(
            listOf(
                AccountTitleViewEntity(titleId = R.string.fragment_account_title_1),
                AccountTitleViewEntity(titleId = R.string.fragment_account_title_2),
                AccountTitleViewEntity(titleId = R.string.fragment_account_title_3),
            )
        )
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