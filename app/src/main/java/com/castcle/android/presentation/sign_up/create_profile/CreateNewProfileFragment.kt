package com.castcle.android.presentation.sign_up.create_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.databinding.FragmentCreateNewProfileBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.sign_up.create_profile.entity.RegisterRequest
import com.castcle.android.presentation.sign_up.entity.*
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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
//  Created by sklim on 6/9/2022 AD at 10:46.

class CreateNewProfileFragment : BaseFragment() {

    private val viewModel by viewModel<NewProfileViewModel>()

    private val binding by lazy {
        FragmentCreateNewProfileBinding.inflate(layoutInflater)
    }

    private val args by navArgs<CreateNewProfileFragmentArgs>()

    private val directions = CreateNewProfileFragmentDirections

    private val profileBuild: ProfileBundle
        get() = args.profileBundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.inputUiState.collectLatest {
                it?.let { uiState ->
                    handlerUiState(uiState)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.suggestionUiState.collectLatest {
                it?.let {
                    handlerSuggestCastcleID(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.registerUiState.collectLatest {
                it?.let {
                    handlerRegisterOrCreateState(it)
                }
            }
        }
    }

    private fun handlerSuggestCastcleID(uiState: BaseUiState<String>) {
        when (uiState) {
            is BaseUiState.Success -> {
                binding.itInCastcleId.run {
                    if (text?.isBlank() == true) {
                        setText(uiState.data)
                    }
                }
            }
            else -> Unit
        }
    }

    override fun initViewProperties() {
        super.initViewProperties()

        when (profileBuild) {
            is ProfileBundle.CreateProfile -> {
                viewModel.createUserState.value = CreateUserState.PROFILE_CREATE
                initActionBar(getString(R.string.fragment_new_profile_bar_profile))
                binding.tvWelcome.text = getString(R.string.fragment_new_profile_message)
                (profileBuild as ProfileBundle.CreateProfile).also {
                    viewModel.email.value = it.email
                    viewModel.password.value = it.password
                }
            }
            is ProfileBundle.CreatePage -> {
                viewModel.createUserState.value = CreateUserState.PAGE_CREATE
                initActionBar(getString(R.string.fragment_new_profile_bar_page))
                binding.tvWelcome.text = getString(R.string.fragment_new_page_message)
            }
        }

        with(binding) {
            with(itInCastcleId) {
                addTextChangedListener(
                    TextChangeCastcleIdListener(this,
                        onTextChanged = {
                            viewModel.handlerDisplayName(it)
                        })
                )
            }

            compositeDisposable += itInDisplayName.onTextChange {
                viewModel.getSuggestionCastcleID(it)
                enableBottomNext(binding.itInCastcleId.text?.isNotBlank() == true)
            }

            btNext.onClick {
                onRegisterOrCreate()
            }
        }
    }

    private fun onRegisterOrCreate() {
        viewModel.onRegisterWithEmail(
            RegisterRequest(
                displayName = binding.itInDisplayName.text.toString(),
                castcleId = binding.itInCastcleId.text.toString(),
                email = viewModel.email.value,
                password = viewModel.password.value
            )
        )
    }

    private fun handlerRegisterOrCreateState(baseUiState: BaseUiState<UserEntity>) {
        when (baseUiState) {
            is BaseUiState.Success -> {
                handlerNavigateNext(baseUiState.data)
            }
            is BaseUiState.Loading -> {
                if (baseUiState.isLoading) {
                    showLoading()
                } else {
                    dismissLoading()
                }
            }
            is BaseUiState.Error -> Unit
            else -> Unit
        }
    }

    private fun handlerNavigateNext(data: UserEntity?) {
        val bundle = when (viewModel.createUserState.value) {
            CreateUserState.PROFILE_CREATE -> {
                (profileBuild as ProfileBundle.CreateProfile).apply {
                    castcleId = data?.castcleId ?: binding.itInCastcleId.text.toString()
                    displayName = binding.itInDisplayName.text.toString()
                }
            }
            CreateUserState.PAGE_CREATE ->
                (profileBuild as ProfileBundle.CreatePage).apply {
                    castcleId = data?.castcleId ?: binding.itInCastcleId.text.toString()
                    displayName = binding.itInDisplayName.text.toString()
                }
            else -> profileBuild
        }
        directions.toUpdateprofileFragment(bundle).navigate()
    }

    private fun handlerUiState(verifyProfileState: VerifyProfileState) {
        when (verifyProfileState) {
            VerifyProfileState.CASTCLE_ID_ERROR -> {
                enableBottomNext()
                handleErrorState(
                    getString(R.string.fragment_new_profile_warning_message),
                    true
                )
            }
            VerifyProfileState.CASTCLE_ID_LENGHT_ERROR -> {
                enableBottomNext()
                handleErrorState(
                    getString(R.string.fragment_new_profile_castcle_id_error_limit_char),
                    true
                )
            }
            VerifyProfileState.CASTCLE_ID_SPECIAL_ERROR -> {
                enableBottomNext()
                handleErrorState(
                    getString(R.string.fragment_new_profile_castcle_id_error_special_char),
                    true
                )
            }
            VerifyProfileState.CASTCLE_ID_PASS -> {
                enableBottomNext(binding.itInDisplayName.text?.isNotBlank() == true)
            }
            else -> {
                handleErrorState()
                enableBottomNext(false)
            }
        }
    }

    private fun enableBottomNext(isPass: Boolean = false) {
        with(binding.btNext) {
            setStatePass(isPass)
            isEnabled = isPass
        }
    }

    private fun handleErrorState(message: String = "", error: Boolean = false) {
        with(binding.tvErrorMessage) {
            visibleOrGone(error)
            text = message
        }
    }

    private fun initActionBar(titleName: String) {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = titleName,
        )
    }

    override fun initObserver() {

    }
}