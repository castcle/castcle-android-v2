package com.castcle.android.presentation.sign_up.update_profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.checkPermissionCamera
import com.castcle.android.core.extensions.getIntentImagePicker
import com.castcle.android.core.work.StateWorkLoading
import com.castcle.android.databinding.FragmentUpdateProfileBinding
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.sign_up.create_profile.CreateNewProfileFragmentArgs
import com.castcle.android.presentation.sign_up.entity.ProfileBundle
import com.castcle.android.presentation.sign_up.update_profile.entity.PhotoSelectedState
import com.castcle.android.presentation.sign_up.update_profile.item_update_profile.UpdateProfileViewRenderer
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.FlowPreview
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
//  Created by sklim on 6/9/2022 AD at 17:46.

class UpdateProfileFragment : BaseFragment(), UpdateProfileListener {

    private val viewModel by viewModel<UpdateProfileViewModel>()

    private val binding by lazy {
        FragmentUpdateProfileBinding.inflate(layoutInflater)
    }

    private val args by navArgs<CreateNewProfileFragmentArgs>()

    private val profileBuild: ProfileBundle
        get() = args.profileBundle

    private val directions = UpdateProfileFragmentDirections

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(UpdateProfileViewRenderer())
        }
    }

    private val launcherPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let {
                    when (viewModel.selectState.value) {
                        PhotoSelectedState.AVATAR_SELECT ->
                            lifecycleScope.launch {
                                viewModel.imageAvatarUri.emit(it)
                            }
                        PhotoSelectedState.COVER_SELECT ->
                            lifecycleScope.launch {
                                viewModel.imageCoverUri.emit(it)
                            }
                        else -> Unit
                    }
                }
            }
        }

    private fun handlerOptionalSelect() {
        directions.toOptionDialogFragment(OptionDialogType.CameraOption).navigate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initListener() {
        setFragmentResultListener(OPTIONAL_SELECT) { key, bundle ->
            if (key == OPTIONAL_SELECT) {
                val result = bundle.getInt(OPTIONAL_SELECT)
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
    }

    private fun onRequestPermission(onGrantPass: () -> Unit) {
        checkPermissionCamera(onGrant = {
            onGrantPass.invoke()
        })
    }

    override fun initViewProperties() {
        super.initViewProperties()
        addOnBackPressedCallback()

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

        with(binding) {
            recyclerView.adapter = adapter
        }

        viewModel.checkWorkUpLoadImage()?.subscribe {
            if (it == StateWorkLoading.SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.upload_image_success),
                    Toast.LENGTH_SHORT
                ).show()
                when (viewModel.selectState.value) {
                    PhotoSelectedState.AVATAR_SELECT ->
                        true
                    PhotoSelectedState.COVER_SELECT ->
                        false
                    else -> null
                }?.let {
                    viewModel.onStopLoading(it)
                }
            }
        }?.addTo(compositeDisposable)
    }

    @FlowPreview
    override fun initConsumer() {
        when (profileBuild) {
            is ProfileBundle.CreateProfile -> {
                viewModel.getUserLocal((profileBuild as ProfileBundle.CreateProfile).castcleId)
            }
            is ProfileBundle.CreatePage -> {
                viewModel.getUserLocal((profileBuild as ProfileBundle.CreatePage).castcleId)
            }
        }

        lifecycleScope.launch {
            viewModel.updateProfileItem.collectLatest(adapter::submitList)
        }
    }

    override fun onUpdateAvatar() {
        viewModel.selectState.value = PhotoSelectedState.AVATAR_SELECT
        handlerOptionalSelect()
    }

    override fun onUpdateCover() {
        viewModel.selectState.value = PhotoSelectedState.COVER_SELECT
        handlerOptionalSelect()
    }

    override fun onNext() {
        handleNextStep()
    }

    private fun handleNextStep() {
        directions.toEditNewProfileFragment(profileBuild).navigate()
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

    companion object {
        const val OPTIONAL_SELECT = "optional-select"
    }
}
