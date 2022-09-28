package com.castcle.android.presentation.profile.edit_profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.databinding.FragmentEditProfileBinding
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.setting.create_page.insert.InsertFragment.Companion.INSERT_DATA_SUCCESS
import com.castcle.android.presentation.setting.create_page.insert.entity.InsertEntity
import com.castcle.android.presentation.sign_up.entity.ProfileBundle
import com.castcle.android.presentation.sign_up.entity.VerifyProfileState
import com.castcle.android.presentation.sign_up.update_profile.UpdateProfileFragment
import com.castcle.android.presentation.sign_up.update_profile.UpdateProfileListener
import com.castcle.android.presentation.sign_up.update_profile.entity.PhotoSelectedState
import com.castcle.android.presentation.sign_up.update_profile_detail.LIMIT_OVERVIEW
import com.stfalcon.imageviewer.StfalconImageViewer
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
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
//  Created by sklim on 25/9/2022 AD at 16:16.

class EditProfileFragment : BaseFragment(), UpdateProfileListener {

    private val viewModel by stateViewModel<EditProfileViewModel>()

    private val binding by lazy {
        FragmentEditProfileBinding.inflate(layoutInflater)
    }

    private val args by navArgs<EditProfileFragmentArgs>()

    private val profileBuild: ProfileBundle
        get() = args.profileBundle

    private val directions = EditProfileFragmentDirections

    private val launcherPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let {
                    when (viewModel.selectState.value) {
                        PhotoSelectedState.AVATAR_SELECT ->
                            lifecycleScope.launch {
                                viewModel.imageAvatarUri.value = it
                            }
                        PhotoSelectedState.COVER_SELECT ->
                            lifecycleScope.launch {
                                viewModel.imageCoverUri.value = it
                            }
                        else -> Unit
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initConsumer() {
        super.initConsumer()

        when (profileBuild) {
            is ProfileBundle.User -> {
                viewModel.userType.value = UserType.People
                viewModel.displayName.value = (profileBuild as ProfileBundle.User).displayName
                viewModel.fetchProfile((profileBuild as ProfileBundle.User).userId)
            }
            is ProfileBundle.Page -> {
                viewModel.userType.value = UserType.Page
                viewModel.displayName.value = (profileBuild as ProfileBundle.Page).displayName
                viewModel.fetchProfile((profileBuild as ProfileBundle.Page).userId)
            }
            else -> Unit
        }

        lifecycleScope.launch {
            viewModel.updateUiState.collectLatest {
                handlerUiState(it)
            }
        }

        lifecycleScope.launch {
            viewModel.inputUiState.collectLatest {
                it?.let { uiState ->
                    handlerUiCastcleUiState(uiState)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.overviewStatePass
                .drop(1)
                .distinctUntilChangedBy { it }
                .collectLatest {
                    handlerButtonState(it)
                }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemView.collectLatest { itemView ->
                    itemView?.let {
                        bindProfileData(it)
                    }
                }
            }
        }
    }

    override fun initListener() {
        setFragmentResultListener(UpdateProfileFragment.OPTIONAL_SELECT) { key, bundle ->
            if (key == UpdateProfileFragment.OPTIONAL_SELECT) {
                val result = bundle.getInt(UpdateProfileFragment.OPTIONAL_SELECT)
                when (viewModel.selectState.value) {
                    PhotoSelectedState.AVATAR_SELECT ->
                        requireActivity().getIntentImagePicker(result, true)
                    PhotoSelectedState.COVER_SELECT ->
                        requireActivity().getIntentImagePicker(result, false)
                    else -> null
                }.let {
                    it?.run {
                        onRequestPermission(onGrantPass = {
                            launcherPicker.launch(it)
                        })
                    }
                }
            }
        }

        setFragmentResultListener(INSERT_DATA_SUCCESS) { key, bundle ->
            if (key == INSERT_DATA_SUCCESS) {
                val result = bundle.getBoolean(INSERT_DATA_SUCCESS)
                handlerButtonState(result && viewModel.overviewStatePass.value)
            }
        }
    }

    private fun onRequestPermission(onGrantPass: () -> Unit) {
        checkPermissionCamera(onGrant = {
            onGrantPass.invoke()
        })
    }

    override fun initObserver() {
        viewModel.displayName.observe(viewLifecycleOwner) {
            initActionBar(it)
        }
    }

    private fun bindProfileData(bindItem: ItemEditEntity) {
        with(binding) {
            compositeDisposable += binding.itOverView.onTextChange {
                bindItem.overview = it
                onBindOverviewCount(it.length)
            }

            bindItem.userEntity.let { it ->
                ivAvatar.loadAvatarImageLocal(
                    imageUri = bindItem.avatarUpLoad,
                    it.avatar.original
                )
                ivCover.loadScaleCenterCrop(
                    scale = 12 to 10,
                    thumbnailUrl = it.cover?.thumbnail,
                    uri = bindItem.coveUpLoad, url = it.cover?.original?.ifBlank { null }
                )

                with(binding.itCastcleId) {
                    setText(it.castcleId)
                    isEnabled = it.canUpdateCastcleId == false

                    addTextChangedListener(
                        TextChangeCastcleIdListener(this,
                            onTextChanged = {
                                if (it.isNotBlank() && it != viewModel.userEntity.value.castcleId) {
                                    bindItem.castcleId = it
                                    viewModel.handlerDisplayName(it)
                                }

                                handlerButtonState(
                                    it.isNotBlank()
                                )
                            })
                    )
                }

                it.overview?.let {
                    itOverView.setText(it)
                }

                if (it.displayName.isNotBlank()) {
                    itDisplayName.setText(it.displayName)
                }
                compositeDisposable += itDisplayName.onTextChange {
                    bindItem.displayName = it
                    handlerButtonState(it.isNotBlank())
                }

                ivCastcleIdWarning.visibleOrGone(it.canUpdateCastcleId == true)

                if (bindItem.userEntity.type == UserType.People) {
                    itemDetailPage.clDetailPage.gone()
                    itemDetailProfile.tvBirthdayDescription.setTextColorState(it.dob?.isNotBlank() == true)
                    itemDetailProfile.tvBirthdayDescription.text =
                        (bindItem.birthDate ?: it.dob)?.toFormatDateBirthDate()
                            ?: requireContext().getString(R.string.none)
                    compositeDisposable += binding.itemDetailProfile.tvBirthdayDescription.onClick {
                        onEditBirthDate(bindItem)
                    }
                } else {
                    itemDetailProfile.clDetailProfile.gone()
                    itemDetailPage.run {
                        clDetailPage.visible()
                        tvEmailDescription.setTextColorState(it.contactEmail?.isNotBlank() == true)
                        tvEmailDescription.text =
                            it.contactEmail ?: requireContext().getString(R.string.none)
                        tvNumberDescription.setTextColorState(it.contactNumber?.isNotBlank() == true)
                        tvNumberDescription.text =
                            it.contactNumber ?: requireContext().getString(R.string.none)
                    }
                }

                with(itLinkFacebook) {
                    it.linkFacebook?.let {
                        setText(it)
                    }
                    addTextChangedListener(
                        TextChangeListener(this, onTextChanged = {
                            bindItem.linkFacebook = it
                            handlerButtonState(
                                it.isNotBlank()
                            )
                        })
                    )
                }

                with(itLinkMedium) {
                    it.linkMedium?.let {
                        setText(it)
                    }
                    addTextChangedListener(
                        TextChangeListener(this, onTextChanged = {
                            bindItem.linkMedium = it
                            handlerButtonState(
                                it.isNotBlank()
                            )
                        })
                    )
                }

                with(itLinkTwitter) {
                    it.linkTwitter?.let {
                        setText(it)
                    }
                    addTextChangedListener(
                        TextChangeListener(this, onTextChanged = {
                            bindItem.linkTwitter = it
                            handlerButtonState(
                                it.isNotBlank()
                            )
                        })
                    )
                }

                with(itLinkYouTube) {
                    it.linkYoutube?.let {
                        setText(it)
                    }
                    addTextChangedListener(
                        TextChangeListener(this, onTextChanged = {
                            bindItem.linkYoutube = it
                            handlerButtonState(
                                it.isNotBlank()
                            )
                        })
                    )
                }

                with(itLinkWeb) {
                    it.linkWebsite?.let {
                        setText(it)
                    }
                    addTextChangedListener(
                        TextChangeListener(this, onTextChanged = {
                            bindItem.linkWeb = it
                            handlerButtonState(
                                it.isNotBlank()
                            )
                        })
                    )
                }
            }
        }
    }

    private fun handlerOptionalSelect() {
        directions.toOptionDialogFragment(OptionDialogType.CameraOption).navigate()
    }

    override fun initViewProperties() {
        compositeDisposable += binding.btDone.onClick {
            onSaveEditProfile()
        }

        compositeDisposable += binding.ivAddAvatar.onClick {
            onUpdateAvatar()
        }

        compositeDisposable += binding.ivAvatar.onClick {
            onImageClicked(listOf(viewModel.userEntity.value.avatar), 0)
        }

        compositeDisposable += binding.ivAddCover.onClick {
            onUpdateCover()
        }

        compositeDisposable += binding.ivCover.onClick {
            viewModel.userEntity.value.cover?.let {
                onImageClicked(listOf(it), 0)
            }
        }

        compositeDisposable += binding.itemDetailPage.tvEmailDescription.onClick {
            onEditContractEmail()
        }

        compositeDisposable += binding.itemDetailPage.tvNumberDescription.onClick {
            onEditContractNumber()
        }

    }

    fun onImageClicked(image: List<ImageEntity>, position: Int) {
        StfalconImageViewer.Builder(context, image, ::loadViewLargeImage)
            .withStartPosition(position)
            .withHiddenStatusBar(true)
            .allowSwipeToDismiss(true)
            .allowZooming(true)
            .show()
    }

    private fun onBindOverviewCount(overviewCount: Int) {
        val (color, isPass) = when {
            overviewCount <= LIMIT_OVERVIEW -> {
                R.color.white to true
            }
            else -> {
                R.color.red_3 to false
            }
        }

        binding.tvOverCount.run {
            text = (LIMIT_OVERVIEW - overviewCount).toString()
            setTextColor(color(color))
        }

        handlerButtonState(
            binding.itOverView.text.toString().isNotBlank() &&
                binding.itOverView.text.toString() != viewModel.userEntity.value.overview
        )

        onEditOverviewState(isPass)
    }

    private fun handlerButtonState(isPass: Boolean = false) {
        var finalState = false
        viewModel.handlerOnCastcleIdEdit(
            isPass,
            onPass = {
                finalState = true
            }, onFail = {
                finalState = false
            })

        binding.btDone.run {
            setStatePass(finalState)
            isEnabled = finalState
        }
    }

    private fun onSaveEditProfile() {
        viewModel.onUpdateProfile()
    }

    override fun onUpdateAvatar() {
        viewModel.selectState.value = PhotoSelectedState.AVATAR_SELECT
        handlerOptionalSelect()
    }

    override fun onUpdateCover() {
        viewModel.selectState.value = PhotoSelectedState.COVER_SELECT
        handlerOptionalSelect()
    }

    override fun onEditContractNumber() {
        navigateToInsertFragment(
            InsertEntity.InsertContractNumber(
                viewModel.userEntity.value.castcleId
            )
        )
    }

    override fun onEditContractEmail() {
        navigateToInsertFragment(
            InsertEntity.InsertEmail(
                viewModel.userEntity.value.castcleId
            )
        )
    }

    private fun navigateToInsertFragment(insertEntity: InsertEntity) {
        directions.toInsertFragment(insertEntity).navigate()
    }

    override fun onEditOverviewState(isPass: Boolean) {
        viewModel.overviewStatePass.value = isPass
    }

    override fun onEditBirthDate(bindItem: ItemEditEntity) {
        requireContext().openEditBirthDate(
            viewModel.itemView.value?.birthDate ?: viewModel.userEntity.value.dob
        ) {
            onHandlerEditBirthDate(it, bindItem)
        }
    }

    private fun onHandlerEditBirthDate(doDate: Date, bindItem: ItemEditEntity) {
        doDate.toFormatString(COMMON_DOB_DATE_FORMAT).also {
            bindItem.birthDate = it
            handlerButtonState(true)
        }
    }

    private fun initActionBar(titleName: String) {
        binding.actionBar.bind(
            rightButtonAction = {
                backPress()
            },
            title = titleName,
        )
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
                backPress()
            }
            else -> Unit
        }
    }

    private fun handlerUiCastcleUiState(verifyProfileState: VerifyProfileState) {
        when (verifyProfileState) {
            VerifyProfileState.CASTCLE_ID_ERROR -> {
                handlerButtonState()
                handleErrorState(
                    getString(R.string.fragment_new_profile_warning_message),
                    true
                )
            }
            VerifyProfileState.CASTCLE_ID_LENGHT_ERROR -> {
                handlerButtonState()
                handleErrorState(
                    getString(R.string.fragment_new_profile_castcle_id_error_limit_char),
                    true
                )
            }
            VerifyProfileState.CASTCLE_ID_SPECIAL_ERROR -> {
                handlerButtonState()
                handleErrorState(
                    getString(R.string.fragment_new_profile_castcle_id_error_special_char),
                    true
                )
            }
            VerifyProfileState.CASTCLE_ID_PASS -> {
                handleErrorState()
                handlerButtonState(viewModel.overviewStatePass.value)
            }
            VerifyProfileState.NONE -> {
                handleErrorState()
                handlerButtonState(false)
            }
            else -> Unit
        }
    }

    private fun handleErrorState(message: String = "", error: Boolean = false) {
        with(binding.tvErrorMessage) {
            visibleOrGone(error)
            text = message
        }
    }
}