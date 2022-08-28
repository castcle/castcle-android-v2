package com.castcle.android.presentation.setting.update_profile_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success.UpdateProfileSuccessViewEntity
import com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success.UpdateProfileSuccessViewRenderer

class UpdateProfileSuccessFragment : BaseFragment(), UpdateProfileSuccessListener {

    private val args by navArgs<UpdateProfileSuccessFragmentArgs>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonIcon = null,
            title = when (args.otp.objective) {
                is OtpObjective.VerifyMobile -> R.string.mobile_number
            },
        )
        adapter.submitList(listOf(getItems()))
    }

    private fun getItems() = UpdateProfileSuccessViewEntity(
        description = when (args.otp.objective) {
            is OtpObjective.VerifyMobile -> string(R.string.fragment_update_profile_success_title_2)
        },
        icon = when (args.otp.objective) {
            is OtpObjective.VerifyMobile -> R.drawable.ic_update_profile_success
        },
        title = when (args.otp.objective) {
            is OtpObjective.VerifyMobile -> string(R.string.fragment_update_profile_success_title_1)
        },
    )

    override fun onCloseClicked() {
        backPress()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(UpdateProfileSuccessViewRenderer())
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