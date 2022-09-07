package com.castcle.android.presentation.sign_up

import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.constants.URL_PRIVACY_POLICY
import com.castcle.android.core.constants.URL_TERMS_OF_SERVICE
import com.castcle.android.core.extensions.*
import com.castcle.android.data.authentication.entity.AuthExistResponse
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.databinding.FragmentSignUpBinding
import com.castcle.android.presentation.sign_up.entity.ProfileBundle
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
//  Created by sklim on 5/9/2022 AD at 12:04.

class SignUpFragment : BaseFragment() {

    private val viewModel by viewModel<SignUpViewModel>()

    private val binding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    private val directions = SignUpFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initViewProperties() {
        super.initViewProperties()
        setupTermsPrivacyText()
        setupAlreadyLoginText()

        with(binding.signUpPage) {
            compositeDisposable += ivBack.onClick {
                backPress()
            }

            ieInEmail.run {
                filters = arrayOf(InputFilter.LengthFilter(250))
                compositeDisposable += onTextChange {
                    checkEmailExist(it)
                }
            }

            compositeDisposable += etInPassword.onTextChange {
                viewModel.createPassword(password = it)
            }

            compositeDisposable += etInConfirmPassword.onTextChange {
                viewModel.createPassword(confirmPass = it)
            }

            cbAccept.setOnCheckedChangeListener { _, _ ->
                handleNextStep()
            }

            compositeDisposable += btContinue.onClick {
                navigateToCreateNewProfile()
            }
        }
    }

    private fun navigateToCreateNewProfile() {
        val profileBundle: ProfileBundle = ProfileBundle.CreateProfile(
            email = binding.signUpPage.ieInEmail.text.toString(),
            password = binding.signUpPage.etInPassword.text.toString()
        )
        directions.toCreateNewProfileFragment(profileBundle).navigate()
    }

    private fun setupTermsPrivacyText() {
        val text = SpannableString(getString(R.string.fragment_sign_up_terms_and_privacy))
        val textColor = requireContext().getColor(R.color.blue)
        val textClickListener1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                openUrl(URL_TERMS_OF_SERVICE)
            }
        }
        val textClickListener2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                openUrl(URL_PRIVACY_POLICY)
            }
        }
        text.setSpan(textClickListener1, 13, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(textClickListener2, 45, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(ForegroundColorSpan(textColor), 13, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(ForegroundColorSpan(textColor), 45, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUpPage.tvTermsService.movementMethod = LinkMovementMethod.getInstance()
        binding.signUpPage.tvTermsService.text = text
    }

    private fun setupAlreadyLoginText() {
        val text = SpannableString(getString(R.string.fragment_sign_up_already_login))
        val textColor = requireContext().getColor(R.color.blue)
        val textClickListener1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                backPress()
            }
        }

        text.setSpan(textClickListener1, 25, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(ForegroundColorSpan(textColor), 25, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUpPage.tvTermsService.movementMethod = LinkMovementMethod.getInstance()
        binding.signUpPage.tvTermsService.text = text
    }

    private fun checkEmailExist(email: String) {
        when {
            email.isBlank() -> {
                showErrorMessage()
            }
            !email.isEmail() -> {
                showErrorMessage(
                    message = R.string.fragment_sign_up_email_error_format,
                    exist = true
                )
            }
            email.isNotBlank() -> {
                viewModel.onCheckEmailIsExist(email)
            }
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.emailVerifyUiState.collectLatest {
                handlerVerifyEmailState(it)
            }
        }

        lifecycleScope.launch {
            viewModel.passwordUiState.collect {
                handlerVerifyPasswordState(it)
            }
        }
    }

    override fun initObserver() {
        viewModel.emailState.observe(viewLifecycleOwner) {
            showErrorMessage(exist = !it)
        }
    }

    private fun showErrorMessage(
        message: Int = R.string.fragment_sign_up_email_verify_error,
        exist: Boolean = false
    ) {
        with(binding.signUpPage.tvErrorMessage) {
            text = getString(message)
            visibleOrGone(exist)
        }
    }

    private fun handlerVerifyPasswordState(uiState: VerifyPasswordUiState?) {
        when (uiState) {
            is VerifyPasswordUiState.OnPasswordMatch -> {
                binding.signUpPage.tvRetryPassError.setStatePass(true)
            }
            is VerifyPasswordUiState.OnCharactersConditionPass -> {
                binding.signUpPage.tvErrorCharacters.setStatePass(true)
                binding.signUpPage.tvErrorCase.setStatePass(true)
            }
            is VerifyPasswordUiState.OnCharactersMinimumPass -> {
                binding.signUpPage.tvErrorCharacters.setStatePass(true)
            }
            is VerifyPasswordUiState.OnCharacterUpperLowerPass -> {
                binding.signUpPage.tvErrorCase.setStatePass(true)
            }
            is VerifyPasswordUiState.OnCharacterUpperLowerError -> {
                binding.signUpPage.tvErrorCase.setStatePass()
            }
            is VerifyPasswordUiState.OnCharactersMinimumError -> {
                binding.signUpPage.tvErrorCharacters.setStatePass()
            }
            is VerifyPasswordUiState.OnPasswordNotMatch -> {
                binding.signUpPage.tvRetryPassError.setStatePass()
            }
            else -> clearAllState()
        }
        handleNextStep()
    }

    private fun clearAllState() {
        with(binding.signUpPage) {
            tvErrorCase.setStatePass()
            tvRetryPassError.setStatePass()
            tvErrorCharacters.setStatePass()
        }
    }

    private fun handlerVerifyEmailState(uiState: BaseUiState<AuthExistResponse>?) {
        when (uiState) {
            is BaseUiState.Success -> {
                viewModel.emailState.value = !uiState.data?.payload?.exist!!
                handleNextStep()
            }
            is BaseUiState.Error -> {
                viewModel.emailState.value = false
                showErrorMessage(exist = true)
            }
            else -> Unit
        }
    }

    private fun handleNextStep() {
        when {
            viewModel.passwordUiState.value is VerifyPasswordUiState.OnPasswordMatch &&
                binding.signUpPage.cbAccept.isChecked && viewModel.emailState.value ?: false -> {
                handleButtonNext(true)
            }
            else -> {
                handleButtonNext()
            }
        }
    }

    private fun handleButtonNext(isPass: Boolean = false) {
        with(binding.signUpPage.btContinue) {
            isEnabled = isPass
            setStatePass(isPass)
        }
    }

}
