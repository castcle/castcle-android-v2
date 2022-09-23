package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.DialogFragmentEndAdStatusBinding
import com.castcle.android.domain.ads.type.AdBoostStatusType
import com.castcle.android.domain.ads.type.AdStatusType
import io.reactivex.rxkotlin.plusAssign

class EndAdStatusDialogFragment(private val onCancel: Boolean) : BaseBottomSheetDialogFragment() {

    private val binding by lazy {
        DialogFragmentEndAdStatusBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_NoTitle)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        return dialog
    }

    override fun initListener() {
        compositeDisposable += binding.btConfirm.onClick {
            if (onCancel) {
                AdStatusType.Canceled.id
            } else {
                AdBoostStatusType.End.id
            }.let {
                handleNavigateResultBack(it)
            }
        }

        compositeDisposable += binding.btCancel.onClick {
            dismiss()
        }
    }

    override fun initViewProperties() {
        if (onCancel) {
            with(binding) {
                tvEndAdsTitle.text = requireContext().getString(R.string.ads_dialog_cancel_title)
                tvEndAdsMessage.text =
                    requireContext().getString(R.string.ads_dialog_cancel_message)
            }
            binding.btConfirm.setTintColor(R.color.yellow_cancel)
            binding.btConfirm.text = string(R.string.ads_dialog_cancel_status)
        }
    }

    private fun handleNavigateResultBack(status: String) {
        requireActivity().supportFragmentManager.setFragmentResult(
            BOOST_STATUS_END,
            getBundleResultBack(status)
        )
        dismiss()
    }

    private fun getBundleResultBack(status: String): Bundle {
        return if (onCancel) {
            bundleOf(BOOST_STATUS_CANCEL to status)
        } else {
            bundleOf(BOOST_STATUS_END to status)
        }
    }

    companion object {
        const val BOOST_STATUS_END = "BOOST_STATUS_END"
        const val BOOST_STATUS_CANCEL = "BOOST_STATUS_CANCEL"

        fun newInstance(onCancel: Boolean = false) = EndAdStatusDialogFragment(onCancel)
    }

}
