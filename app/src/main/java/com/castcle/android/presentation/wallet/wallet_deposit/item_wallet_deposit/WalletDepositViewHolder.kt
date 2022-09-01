package com.castcle.android.presentation.wallet.wallet_deposit.item_wallet_deposit

import android.util.Base64
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.core.glide.GlideApp
import com.castcle.android.databinding.ItemWalletDepositBinding
import com.castcle.android.presentation.wallet.wallet_deposit.WalletDepositListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletDepositViewHolder(
    private val binding: ItemWalletDepositBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletDepositListener,
) : CastcleViewHolder<WalletDepositViewEntity>(binding.root) {

    override var item = WalletDepositViewEntity()

    init {
        compositeDisposable += binding.tvCastcleId.onClick {
            context().copyToClipboard(item.castcleId)
        }
        compositeDisposable += binding.ivSave.onClick {
            binding.shareWallet.toBitmap {
                listener.onSaveQrCodeWallet(it)
            }
        }
        compositeDisposable += binding.ivShare.onClick {
            binding.shareWallet.toBitmap {
                listener.onShareQrCodeWallet(it)
            }
        }
    }

    override fun bind(bindItem: WalletDepositViewEntity) {
        try {
            binding.tvCastcleId.text = item.castcleId
            binding.tvDescription.text = item.castcleId
            GlideApp.with(context())
                .load(Base64.decode(item.qrCode.toByteArray(), Base64.DEFAULT))
                .into(binding.ivQrCode)
            GlideApp.with(context())
                .load(Base64.decode(item.qrCode.toByteArray(), Base64.DEFAULT))
                .into(binding.ivQrCodeExport)
        } catch (exception: Exception) {
            binding.ivQrCode.setImageDrawable(null)
        }
    }

}