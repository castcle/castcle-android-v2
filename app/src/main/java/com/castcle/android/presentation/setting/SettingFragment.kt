package com.castcle.android.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.openUrl
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.setting.item_logout.SettingLogoutViewRenderer
import com.castcle.android.presentation.setting.item_menu.SettingMenuViewRenderer
import com.castcle.android.presentation.setting.item_notification.SettingNotificationViewRenderer
import com.castcle.android.presentation.setting.item_profile_section.SettingProfileSectionViewRenderer
import com.castcle.android.presentation.setting.item_verify_email.SettingVerifyEmailViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment(), SettingListener {

    private val viewModel by viewModel<SettingViewModel>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.setting,
        )
    }

    override fun initObserver() {
        viewModel.logoutError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.logoutComplete.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
    }

    override fun onAccountClick() {

    }

    override fun onLogoutClick() {
        showLoading()
        viewModel.logout()
    }

    override fun onNewPageClick() {

    }

    override fun onNotificationClicked() {

    }

    override fun onUrlClicked(url: String) {
        openUrl(url)
    }

    override fun onUserClicked(user: UserEntity) {
        SettingFragmentDirections.toProfileFragment(user).navigate()
    }

    override fun onVerifyEmailClicked() {

    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(SettingLogoutViewRenderer())
            registerRenderer(SettingMenuViewRenderer())
            registerRenderer(SettingNotificationViewRenderer())
            registerRenderer(SettingProfileSectionViewRenderer())
            registerRenderer(SettingVerifyEmailViewRenderer())
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