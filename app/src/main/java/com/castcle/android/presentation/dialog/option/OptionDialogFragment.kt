package com.castcle.android.presentation.dialog.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.presentation.content.ContentFragment.Companion.REPLY_RESULT
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewRenderer
import com.castcle.android.presentation.dialog.recast.item_recast_title.RecastTitleViewRenderer
import com.castcle.android.presentation.dialog.recast.item_select_recast_user.SelectRecastUserViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class OptionDialogFragment : BaseBottomSheetDialogFragment(), OptionDialogListener {

    private val viewModel by stateViewModel<OptionDialogViewModel> { parametersOf(args.type) }

    private val args by navArgs<OptionDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initObserver() {
        viewModel.getOptionItems(requireContext())
        viewModel.onError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onOptionClicked(eventType: Int) {
        when (val type = args.type) {
            is OptionDialogType.MyCommentOption -> when (eventType) {
                type.deleteComment -> {
                    showLoading()
                    viewModel.deleteComment(type.commentId)
                }
                type.replyComment -> {
                    setFragmentResult(REPLY_RESULT, bundleOf(REPLY_RESULT to type))
                    backPress()
                }
            }
            is OptionDialogType.MyContentOption -> {
                showLoading()
                viewModel.deleteContent(type.contentId)
            }
            is OptionDialogType.MyPageOption -> when (eventType) {
                type.deletePage -> Unit
                type.syncSocialMedia -> Unit
            }
            is OptionDialogType.MyUserOption -> Unit
            is OptionDialogType.OtherCommentOption -> {
                setFragmentResult(REPLY_RESULT, bundleOf(REPLY_RESULT to type))
                backPress()
            }
            is OptionDialogType.OtherContentOption -> {
                OptionDialogFragmentDirections
                    .toReportSubjectFragment(contentId = type.contentId, userId = null)
                    .navigate()
            }
            is OptionDialogType.OtherUserOption -> when (eventType) {
                type.blockUser -> Unit
                type.reportUser -> {
                    OptionDialogFragmentDirections
                        .toReportSubjectFragment(contentId = null, userId = type.userId)
                        .navigate()
                }
            }
            is OptionDialogType.ReplyOption -> {
                showLoading()
                viewModel.deleteReply(type.replyCommentId)
            }
        }
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(OptionDialogViewRenderer())
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