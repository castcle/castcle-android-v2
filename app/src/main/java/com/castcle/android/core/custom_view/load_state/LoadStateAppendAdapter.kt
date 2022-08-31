package com.castcle.android.core.custom_view.load_state

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.BuildConfig
import com.castcle.android.core.extensions.cast
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.LayoutLoadStateAppendBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class LoadStateAppendAdapter(
    private val compositeDisposable: CompositeDisposable,
    private val listener: LoadStateListener,
) : LoadStateAdapter<LoadStateAppendAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutLoadStateAppendBinding.inflate(inflate, parent, false))
    }

    inner class ViewHolder(val binding: LayoutLoadStateAppendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            compositeDisposable += binding.retry.onClick {
                listener.onRetryClicked()
            }
        }

        fun bind(loadState: LoadState) {
            binding.loading.isVisible = loadState is LoadState.Loading
            binding.retry.isVisible = loadState is LoadState.Error
            binding.tvError.isVisible = BuildConfig.DEBUG && loadState is LoadState.Error
            binding.tvError.text = loadState.cast<LoadState.Error>()?.error?.message.orEmpty()
        }

    }

}