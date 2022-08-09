package com.castcle.android.presentation.dialog.recast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.presentation.dialog.item_dialog_option.DialogOptionViewListener
import com.castcle.android.presentation.dialog.item_dialog_option.DialogOptionViewRenderer
import com.castcle.android.presentation.dialog.recast.item_recast_title.RecastTitleViewRenderer
import com.castcle.android.presentation.dialog.recast.item_select_recast_user.SelectRecastUserViewRenderer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class RecastDialogFragment : BaseBottomSheetDialogFragment(), DialogOptionViewListener,
    RecastDialogListener {

    private val viewModel by stateViewModel<RecastDialogViewModel> { parametersOf(args.contentId) }

    private val args by navArgs<RecastDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutParams
            ?.cast<ConstraintLayout.LayoutParams>()
            ?.height = dimenPx(com.intuit.sdp.R.dimen._162sdp)
    }

    override fun initObserver() {
        viewModel.recastFail.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.recastSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
    }

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    override fun onOptionClicked(titleId: Int) {
        when (titleId) {
            R.string.quote_cast -> navigateToCreateQuoteCast()
            R.string.recast, R.string.unrecast -> {
                showLoading()
                viewModel.recast(isRecasted = titleId == R.string.unrecast)
            }
        }
    }

    override fun onSelectUserClicked() {
        viewModel.showSelectUser()
    }

    override fun onUserClicked(userId: String) {
        viewModel.updateSelectedUser(userId)
    }

    private fun navigateToCreateQuoteCast() {
        val currentUserId = viewModel.currentUser.value?.find { it.first }?.second?.id.orEmpty()
        RecastDialogFragmentDirections
            .toNewCastFragment(args.contentId, currentUserId)
            .navigate()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(DialogOptionViewRenderer())
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