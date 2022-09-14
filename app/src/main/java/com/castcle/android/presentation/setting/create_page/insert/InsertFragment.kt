package com.castcle.android.presentation.setting.create_page.insert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.*
import com.castcle.android.data.authentication.entity.AuthExistResponse
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.databinding.FragmentInsertBinding
import com.castcle.android.presentation.dialog.warning.CommonDialogFragment
import com.castcle.android.presentation.dialog.warning.entity.CommonWarningBase
import com.castcle.android.presentation.setting.create_page.insert.entity.InsertEntity
import com.castcle.android.presentation.setting.create_page.insert.item_insert_contract_number.ItemInsertContractNumberViewEntity
import com.castcle.android.presentation.setting.create_page.insert.item_insert_email.ItemInsertEmailViewEntity
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
//  Created by sklim on 12/9/2022 AD at 16:42.

class InsertFragment : BaseFragment(), InsertFragmentListener {

    private val viewModel by viewModel<InsertViewModel>()

    private val binding by lazy {
        FragmentInsertBinding.inflate(layoutInflater)
    }

    private val args by navArgs<InsertFragmentArgs>()

    private val insertEntity: InsertEntity
        get() = args.insertEntity

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.itemView.collectLatest {
                it?.let { itemView ->
                    handlerItemView(itemView)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateUiState.collectLatest {
                handlerVerifyEmailState(it)
            }
        }
    }

    private fun handlerItemView(itemView: CastcleViewEntity) {
        when (itemView) {
            is ItemInsertEmailViewEntity -> {
                onBindViewInsertEmail(itemView)
            }
            is ItemInsertContractNumberViewEntity -> {
                onBindViewInsertContractNumber(itemView)
            }
        }
    }

    private fun onBindViewInsertContractNumber(itemView: ItemInsertContractNumberViewEntity) {
        onBindActionBar(R.string.fragment_insert_contract_number)
        with(binding.insertContractNumber) {
            clInsertContractNumber.visible()
            compositeDisposable += etMobileNumber.onTextChange {
                inputNumber(it)
            }
            compositeDisposable += binding.btSave.onClick {
                onSaveContractNumberClick(etMobileNumber.text.toString())
            }
            handlerButtonSave(itemView.isPass)
        }
    }

    private fun onBindViewInsertEmail(itemView: ItemInsertEmailViewEntity) {
        onBindActionBar(R.string.fragment_insert_email)
        with(binding.insertEmail) {
            clInsertEmail.visible()
            compositeDisposable += etEmail.onTextChange {
                inputEmail(it)
            }
            handlerMessageError(itemView.message)
            itemView.isExists?.let {
                handlerButtonSave(it)
            }
            compositeDisposable += binding.btSave.onClick {
                onSaveEmailClick(etEmail.text.toString())
            }
        }
    }

    private fun onBindActionBar(titleName: Int) {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = titleName,
        )
    }

    private fun handlerMessageError(message: Int?) {
        with(binding.insertEmail.tvErrorMessage) {
            if (message != null) {
                visible()
                string(message)
            } else {
                gone()
            }
        }
    }

    private fun handlerButtonSave(isExist: Boolean) {
        with(binding.btSave) {
            isEnabled = !isExist
            setStatePass(!isExist)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initViewProperties() {
        viewModel.getItemView(insertEntity)
    }

    private fun handlerVerifyEmailState(uiState: BaseUiState<AuthExistResponse>?) {
        when (uiState) {
            is BaseUiState.Loading -> {
                handlerLoading(uiState.isLoading)
            }
            is BaseUiState.SuccessNonBody -> {
                backPress()
            }
            is BaseUiState.Error -> {
                CommonDialogFragment.newInstance(
                    CommonWarningBase.WarningUiModel(
                        titleWarning = requireContext().getString(R.string.warning_permission_required),
                        warningDescription = uiState.exception.message
                            ?: uiState.exception.cause?.message
                            ?: requireContext().getString(R.string.error_base)
                    )
                ).show(childFragmentManager, "")
            }
            else -> Unit
        }
    }

    private fun handlerLoading(isLoading: Boolean = false) {
        if (isLoading) {
            showLoading()
        } else {
            dismissLoading()
        }
    }

    override fun onSaveEmailClick(email: String) {
        viewModel.saveEmail(email)
    }

    override fun onSaveContractNumberClick(number: String) {
        viewModel.saveContractNumber(number)
    }

    override fun inputEmail(email: String) {
        viewModel.onCheckEmail(email)
    }

    override fun inputNumber(number: String) {
        viewModel.onCheckNumber(number)
    }
}