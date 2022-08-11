package com.castcle.android.presentation.new_cast

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentNewCastBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_item.FeedImageItemViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewRenderer
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class NewCastFragment : BaseFragment(), FeedListener {

    private val viewModel by stateViewModel<NewCastViewModel> { parametersOf(args.userId) }

    private val args by navArgs<NewCastFragmentArgs>()

    override fun initViewProperties() {
        updateCastButton()
        updateMessageCount()
        binding.ivSelectedImage.isVisible = args.quoteCastId == null
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (position) {
                    0, 2 -> if (adapter.itemCount == position + 1) 2 else 1
                    else -> 1
                }
            }
        }
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = if (args.quoteCastId != null) {
                R.string.quote_cast
            } else {
                R.string.new_cast
            },
        )
    }

    override fun initListener() {
        compositeDisposable += binding.etMessage.onTextChange {
            updateMessageCount()
            updateCastButton()
        }
        compositeDisposable += binding.clContent.onClick {
            binding.etMessage.showKeyboard()
        }
        compositeDisposable += binding.ivSelectedImage.onClick {
            permissionCallback.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        compositeDisposable += binding.tvCast.onClick {
            showLoading()
            hideKeyboard()
            if (args.quoteCastId != null) {
                viewModel.createQuoteCast(
                    contentId = args.quoteCastId.orEmpty(),
                    message = binding.etMessage.text.toString(),
                )
            } else {
                viewModel.createCast(
                    context = requireContext(),
                    message = binding.etMessage.text.toString(),
                )
            }
        }
    }

    override fun initObserver() {
        viewModel.getQuoteCast(args.quoteCastId)
        viewModel.createContentError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.createContentSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
    }

    private fun updateMessageCount() {
        val messageCount = 280.minus(binding.etMessage.text.length)
        val color = if (messageCount < 0) color(R.color.red_3) else color(R.color.white)
        binding.tvMessageCount.text = messageCount.toString()
        binding.tvMessageCount.setTextColor(color)
    }

    private val selectImageCallback = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        if (it.isNotEmpty()) {
            viewModel.imageUri.value = it.take(4)
        }
    }

    private val permissionCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermissionGrant ->
        if (isPermissionGrant) {
            selectImageCallback.launch("image/*")
        } else {
            toast(string(R.string.warning_permission_required))
        }
    }

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.currentUser.filterNotNull().collectLatest {
                binding.ivAvatar.loadAvatarImage(it.avatar.thumbnail)
                binding.tvDisplayName.text = it.displayName
            }
        }
        lifecycleScope.launch {
            flowOf(viewModel.imageItems, viewModel.quoteCast)
                .flattenMerge()
                .collectLatest {
                    adapter.submitList(it.orEmpty())
                    updateCastButton()
                }
        }
    }

    private fun updateCastButton() {
        val checkImageIsNotEmpty = viewModel.imageUri.value.isNotEmpty()
        val checkMessageIsNotEmpty = binding.etMessage.text.toString().length in 1..280
        val checkMessageLowerThanMaximum = binding.etMessage.text.toString().length <= 280
        val isCanCast = checkImageIsNotEmpty
            .and(checkMessageLowerThanMaximum)
            .or(checkMessageIsNotEmpty)
        binding.tvCast.isEnabled = isCanCast
        binding.tvCast.backgroundTintList = if (isCanCast) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.black_background_1)
        }
        binding.tvCast.foregroundTintList = if (isCanCast) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.black)
        }
        binding.tvCast.setTextColor(
            if (isCanCast) {
                color(R.color.white)
            } else {
                color(R.color.gray_9)
            }
        )
        binding.ivSelectedImage.imageTintList = if (checkImageIsNotEmpty) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.white)
        }
    }

    override fun onDeleteImageClicked(index: Int) {
        viewModel.imageUri.value = viewModel.imageUri.value
            .toMutableList()
            .apply { removeAt(index) }
    }

    override fun onStop() {
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        changeSoftInputMode(true)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(FeedImageViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedImageItemViewRenderer())
            registerRenderer(FeedTextViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedWebViewRenderer(FeedDisplayType.NewCast))
            registerRenderer(FeedWebImageViewRenderer(FeedDisplayType.NewCast))
        }
    }

    private val binding by lazy {
        FragmentNewCastBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}