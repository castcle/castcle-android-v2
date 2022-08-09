package com.castcle.android.presentation.dialog.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewEntity
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewRenderer
import com.castcle.android.presentation.dialog.recast.item_recast_title.RecastTitleViewRenderer
import com.castcle.android.presentation.dialog.recast.item_select_recast_user.SelectRecastUserViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class OptionDialogFragment : BaseBottomSheetDialogFragment(), OptionDialogListener {

    private val viewModel by stateViewModel<OptionDialogViewModel>()

    private val args by navArgs<OptionDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        adapter.submitList(getOptionItems())
    }

    override fun initObserver() {
        viewModel.onError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
    }

    override fun onOptionClicked(titleId: Int) = when (val type = args.type) {
        is OptionDialogType.DeleteContent -> {
            showLoading()
            viewModel.deleteContent(type.contentId)
        }
        is OptionDialogType.ReportContent -> toast("report content")
    }

    private fun getOptionItems(): List<CastcleViewEntity> = when (args.type) {
        is OptionDialogType.DeleteContent -> listOf(
            OptionDialogViewEntity(icon = R.drawable.ic_delete, title = R.string.delete)
        )
        is OptionDialogType.ReportContent -> listOf(
            OptionDialogViewEntity(icon = R.drawable.ic_report, title = R.string.report_cast)
        )
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(OptionDialogViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(RecastTitleViewRenderer())
            registerRenderer(SelectRecastUserViewRenderer())
        }
    }

    private val binding by lazy {
        DialogOptionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}