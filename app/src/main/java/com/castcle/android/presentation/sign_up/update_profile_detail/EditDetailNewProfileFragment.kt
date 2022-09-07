package com.castcle.android.presentation.sign_up.update_profile_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.data.user.entity.UserLinkResponse
import com.castcle.android.databinding.FragmentEditDetailNewProfileBinding
import com.castcle.android.presentation.sign_up.entity.ProfileBundle
import com.castcle.android.presentation.sign_up.update_profile.entity.UserUpdateRequest
import com.castcle.android.presentation.sign_up.update_profile_detail.item_edit_new_profile.EditProfileViewEntity
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
//  Created by sklim on 7/9/2022 AD at 17:28.

class EditDetailNewProfileFragment : BaseFragment(), EditNewProfileListener {

    private val viewModel by viewModel<EditDetailViewModel>()

    private val binding by lazy {
        FragmentEditDetailNewProfileBinding.inflate(layoutInflater)
    }

    private val args by navArgs<EditDetailNewProfileFragmentArgs>()

    private val profileBuild: ProfileBundle
        get() = args.profileBundle

    private val directions = EditDetailNewProfileFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initConsumer() {
        super.initConsumer()
        lifecycleScope.launch {
            viewModel.updateUiState.collectLatest {
                handlerUiState(it)
            }
        }

        lifecycleScope.launch {
            viewModel.editProfileItem.collectLatest {
                handleBindData(it)
            }
        }

        when (profileBuild) {
            is ProfileBundle.CreateProfile -> {
                viewModel.getUserLocal((profileBuild as ProfileBundle.CreateProfile).castcleId)
            }
            is ProfileBundle.CreatePage -> {
                viewModel.getUserLocal((profileBuild as ProfileBundle.CreatePage).castcleId)
            }
        }
    }

    override fun initViewProperties() {
        super.initViewProperties()

        when (profileBuild) {
            is ProfileBundle.CreateProfile -> {
                initActionBar(getString(R.string.fragment_update_profile))
                viewModel.castcleId.value =
                    (profileBuild as ProfileBundle.CreateProfile).castcleId
            }
            is ProfileBundle.CreatePage -> {
                initActionBar(getString(R.string.fragment_update_page))
                viewModel.castcleId.value =
                    (profileBuild as ProfileBundle.CreatePage).castcleId
            }
        }

        with(binding.itemEditProfile) {
            compositeDisposable += tvBirthdayDescription.onClick {
                onPickerBirthDateClick()
            }

            with(itLinkFacebook) {
                addTextChangedListener(
                    TextChangeListener(this,
                        onTextChanged = {
                            handleButtonDone(it.isNotBlank())
                        })
                )
            }

            with(itLinkMedium) {
                addTextChangedListener(
                    TextChangeListener(this,
                        onTextChanged = {
                            handleButtonDone(it.isNotBlank())
                        })
                )
            }

            with(itLinkTwitter) {
                addTextChangedListener(
                    TextChangeListener(this,
                        onTextChanged = {
                            handleButtonDone(it.isNotBlank())
                        })
                )
            }

            with(itLinkYouTube) {
                addTextChangedListener(
                    TextChangeListener(this,
                        onTextChanged = {
                            handleButtonDone(it.isNotBlank())
                        })
                )
            }

            with(itLinkWeb) {
                addTextChangedListener(
                    TextChangeListener(this,
                        onTextChanged = {
                            handleButtonDone(it.isNotBlank())
                        })
                )
            }

            compositeDisposable += itOverView.onTextChange {
                handleButtonDone(it.isNotBlank())
            }

            compositeDisposable += btDone.onClick {
                onConfirmClick(getDataOnUpdate())
            }
        }
    }

    private fun handleBindData(dataState: EditProfileViewEntity) {
        with(binding.itemEditProfile) {
            tvBirthdayDescription.text = dataState.birthDate?.toFormatDateBirthDate()
        }
    }

    private fun handleButtonDone(notBlank: Boolean) {
        binding.itemEditProfile.btDone.run {
            isEnabled = notBlank
            setStatePass(notBlank)
        }
    }

    private fun handlerUiState(uiState: BaseUiState<Nothing>?) {
        when (uiState) {
            is BaseUiState.Error -> Unit
            is BaseUiState.Loading -> {
                if (uiState.isLoading) {
                    showLoading()
                } else {
                    dismissLoading()
                }
            }
            is BaseUiState.SuccessNonBody -> {
                handleNextStep()
            }
            else -> Unit
        }
    }

    private fun initActionBar(titleName: String) {
        binding.actionBar.bind(
            rightMessageAction = R.string.skip,
            rightButtonAction = {
                handleNextStep()
            },
            title = titleName,
        )
    }

    private fun handleNextStep() {
        directions.toProfileFragment(viewModel.userEntity.value)
            .navigate(popUpTo = R.id.loginFragment)
    }

    override fun onPickerBirthDateClick() {
        requireContext().openEditBirthDate(
            viewModel.editProfileItem.value.birthDate ?: ""
        ) {
            onHandlerEditBirthDate(it)
        }
    }

    private fun onHandlerEditBirthDate(doDate: Date) {
        doDate.toFormatString(COMMON_DOB_DATE_FORMAT).run {
            viewModel.onSetBirthDate(this)
        }
    }

    override fun onConfirmClick(updateRequest: UserUpdateRequest) {
        updateRequest.apply {
            currentCastcleId = viewModel.castcleId.value
        }.run(viewModel::onUpdateDetail)
    }

    private fun getDataOnUpdate(): UserUpdateRequest {
        with(binding.itemEditProfile) {
            return UserUpdateRequest(
                dob = tvBirthdayDescription.text.toString().toISO8601(),
                overview = itOverView.text.toString(),
                links = getWebLinkRequest()
            )
        }
    }

    private fun getWebLinkRequest(): UserLinkResponse {
        with(binding.itemEditProfile) {
            return UserLinkResponse(
                facebook = itLinkFacebook.text?.isHasValue(),
                twitter = itLinkTwitter.text?.isHasValue(),
                youtube = itLinkYouTube.text?.isHasValue(),
                website = itLinkWeb.text?.isHasValue(),
                medium = itLinkMedium.text?.isHasValue(),
            )
        }
    }
}