package com.castcle.android.presentation.resent_verify_email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.LayoutRecyclerViewBinding

class ResentVerifyEmailFragment : BaseFragment() {

    override fun initViewProperties() {
        toast("Please verify email.")
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.setting,
        )
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