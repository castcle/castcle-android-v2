/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.splash_screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.activity.BaseActivity
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ActivitySplashScreenBinding
import com.castcle.android.databinding.DialogBasicBinding
import com.castcle.android.domain.setting.entity.ConfigEntity
import com.castcle.android.domain.setting.entity.UpdateVersionEntity
import com.castcle.android.presentation.dialog.basic.BasicDialog
import com.castcle.android.presentation.home.HomeActivity
import com.castcle.android.presentation.splash_screen.dialog_update_version.UpdateVersionDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    private val viewModel by viewModel<SplashScreenViewModel>()

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.config.filterNotNull().collectLatest {
                checkUpdateAvailable(config = it)
            }
        }
        lifecycleScope.launch {
            viewModel.fetchAccessTokenComplete.collectLatest {
                navigate<HomeActivity>(finishCurrent = true)
            }
        }
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                BasicDialog(
                    binding = DialogBasicBinding.inflate(layoutInflater),
                    button = string(R.string.retry),
                    isCancelable = false,
                    title = it.message.orEmpty(),
                ) {
                    viewModel.fetchAccessToken()
                }.show()
            }
        }
    }

    private fun checkUpdateAvailable(config: ConfigEntity) {
        when {
            config.forceUpdateVersion.version > BuildConfig.VERSION_CODE ->
                showUpdateVersionDialog(config.forceUpdateVersion)
            config.playStoreUpdateVersion.version > BuildConfig.VERSION_CODE ->
                showUpdateVersionDialog(config.playStoreUpdateVersion)
            else -> viewModel.fetchAccessToken()
        }
    }

    private fun showUpdateVersionDialog(updateVersion: UpdateVersionEntity) {
        UpdateVersionDialog(
            onNegativeButtonClick = { viewModel.fetchAccessToken() },
            onPositiveButtonClick = { openUrl(updateVersion.updateUrl) },
            updateVersion = updateVersion,
        ).also {
            supportFragmentManager.commit(allowStateLoss = true) { add(it, null) }
        }
    }

    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewProperties()
        initListener()
        initObserver()
    }

}