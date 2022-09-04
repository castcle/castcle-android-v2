package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send

import android.text.InputFilter
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletSendBinding
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType
import com.castcle.android.presentation.wallet.wallet_send.WalletSendListener
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_add_shortcut.WalletSendAddShortcutViewEntity
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_add_shortcut.WalletSendAddShortcutViewRenderer
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_shortcut.WalletSendShortcutViewRenderer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.text.NumberFormat

class WalletSendViewHolder(
    private val binding: ItemWalletSendBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletSendListener,
) : CastcleViewHolder<WalletSendViewEntity>(binding.root), WalletSendViewListener {

    override var item = WalletSendViewEntity()

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletSendAddShortcutViewRenderer())
            registerRenderer(WalletSendShortcutViewRenderer())
        }
    }

    private val decimalFilter = InputFilter { newText, _, _, currentText, _, _ ->
        if (currentText.matches("[0-9]*+((\\.[0-9]{0,7}?)?)|(\\.)?".toRegex())) {
            newText
        } else {
            ""
        }
    }

    init {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.etAmount.filters += decimalFilter
        compositeDisposable += binding.etAmount.onTextChange {
            val amount = it.toDoubleOrNull() ?: 0.0
            if (amount > item.balance) {
                binding.etAmount.setText(item.amount.toString())
                binding.etAmount.setSelection(binding.etAmount.length())
            } else {
                item.amount = amount
                updateSendButton()
            }
        }
        compositeDisposable += binding.etMemo.onTextChange {
            item.memo = it
        }
        compositeDisposable += binding.etNote.onTextChange {
            item.note = it
        }
        compositeDisposable += binding.tvMaxAmount.onClick {
            item.amount = item.balance
            binding.etAmount.setText(item.amount.toString())
            binding.etAmount.setSelection(binding.etAmount.length())
        }
        compositeDisposable += binding.ivScanCastcleId.onClick {
            listener.onScanQrCodeClicked(WalletScanQrCodeRequestType.FromAddress(item.userId))
        }
        compositeDisposable += binding.ivScanMemoId.onClick {
            listener.onScanQrCodeClicked(WalletScanQrCodeRequestType.FromMemo(item.userId))
        }
    }

    override fun bind(bindItem: WalletSendViewEntity) {
        updateSendButton()
        binding.tvSendTo.text = item.targetCastcleId
        binding.etMemo.setText(item.memo)
        binding.etNote.setText(item.note)
        binding.etAmount.setText(
            if (item.amount == 0.0) {
                ""
            } else {
                item.amount.toString()
            }
        )
        binding.tvMaxBalance.text = context().getString(
            R.string.fragment_wallet_send_title_8,
            item.balance.toCastToken(),
        )
        adapter.submitList(item.shortcut.plus(WalletSendAddShortcutViewEntity()))
    }

    override fun onAddShortcutClicked() {
        listener.onAddShortcutClicked()
    }

    override fun onShortcutClicked(castcleId: String, userId: String) {
        item.targetCastcleId = castcleId
        item.targetUserId = userId
        binding.tvSendTo.text = item.targetCastcleId
        updateSendButton()
    }

    private fun Double.toCastToken(): String {
        val numberFormat = NumberFormat.getNumberInstance().apply {
            maximumFractionDigits = 8
        }
        return "${numberFormat.format(this)} CAST"
    }

    private fun updateSendButton() {
        listener.onUpdateSendButton(
            enabled = (item.amount > 0 && item.amount <= item.balance)
                && item.targetUserId.isNotBlank(),
            amount = item.amount,
        )
    }

}