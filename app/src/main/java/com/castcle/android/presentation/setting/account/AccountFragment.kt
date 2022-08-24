package com.castcle.android.presentation.setting.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.setting.account.item_menu.AccountMenuViewRenderer
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
    }

    override fun onDeleteAccountClicked() = Unit

    override fun onLinkFacebookClicked() = Unit

    override fun onLinkTwitterClicked() = Unit

    override fun onMobileNumberClicked() = Unit

    override fun onPasswordClicked() = Unit

    override fun onResentVerifyEmailClicked() = Unit

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