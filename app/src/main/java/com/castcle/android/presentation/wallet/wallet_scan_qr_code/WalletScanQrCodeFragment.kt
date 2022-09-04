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

package com.castcle.android.presentation.wallet.wallet_scan_qr_code

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentWalletScanQrCodeBinding
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WalletScanQrCodeFragment : BaseFragment() {

    private val args by navArgs<WalletScanQrCodeFragmentArgs>()

    private val directions = WalletScanQrCodeFragmentDirections

    override fun initViewProperties() {
        cameraPermissionCallback.launch(Manifest.permission.CAMERA)
    }

    override fun initListener() {
        compositeDisposable += binding.ivBack.onClick {
            backPress()
        }
        compositeDisposable += binding.tvResult.onClick {
            context?.copyToClipboard(binding.tvResult.text.toString())
        }
        compositeDisposable += binding.ivImage.onClick {
            selectImageCallback.launch("image/*")
        }
        compositeDisposable += binding.clMyQrCode.onClick {
            directions.toWalletDepositFragment(args.requestType.userId).navigate()
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            binding.scanQrCodeView.onGetResult().collectLatest {
                val castcleData = getCastcleDataFromQrCode(it)
                if (castcleData != null) {
                    handleCastcleDataFromQrCode(castcleData.first, castcleData.second)
                } else {
                    handleDataFromQrCode(it)
                }
            }
        }
    }

    private fun getCastcleDataFromQrCode(url: String): Pair<String, String>? {
        val split = url.split("|")
        val castcleId = split.getOrNull(2) ?: return null
        val userId = split.getOrNull(1) ?: return null
        return castcleId to userId
    }

    private fun handleCastcleDataFromQrCode(castcleId: String, userId: String) {
        when (args.requestType) {
            is WalletScanQrCodeRequestType.FromAddress -> {
                val bundle = bundleOf(
                    RESULT_CASTCLE_ID to castcleId,
                    RESULT_USER_ID to userId,
                )
                setFragmentResult(ADDRESS_RESULT, bundle)
                backPress()
            }
            is WalletScanQrCodeRequestType.FromDashboard -> directions.toWalletSendFragment(
                targetCastcleId = castcleId,
                targetUserId = userId,
                userId = args.requestType.userId,
            ).navigate(
                popUpTo = R.id.walletScanQrCodeFragment,
            )
            is WalletScanQrCodeRequestType.FromMemo -> {
                setFragmentResult(MEMO_RESULT, bundleOf(RESULT_MEMO to castcleId))
                backPress()
            }
        }
    }

    private fun handleDataFromQrCode(text: String) {
        if (URLUtil.isValidUrl(text)) {
            openUrl(text)
        } else {
            binding.groupResult.visible()
            binding.tvResult.text = text
        }
    }

    private val cameraPermissionCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermissionGrant ->
        if (isPermissionGrant) {
            binding.scanQrCodeView.init(viewLifecycleOwner)
        } else {
            toast(string(R.string.warning_permission_required))
            backPress()
        }
    }

    private val selectImageCallback = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.scanQrCodeView.getResultFromUri(uri)
        }
    }

    private val binding by lazy {
        FragmentWalletScanQrCodeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    companion object {
        const val ADDRESS_RESULT = "ADDRESS_RESULT"
        const val MEMO_RESULT = "MEMO_RESULT"
        const val RESULT_CASTCLE_ID = "RESULT_CASTCLE_ID"
        const val RESULT_MEMO = "RESULT_MEMO"
        const val RESULT_USER_ID = "RESULT_USER_ID"
    }

}