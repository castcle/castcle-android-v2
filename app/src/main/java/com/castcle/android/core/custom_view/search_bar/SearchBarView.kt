package com.castcle.android.core.custom_view.search_bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.onTextChange
import com.castcle.android.databinding.LayoutSearchBarBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SearchBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = LayoutSearchBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    private var onTextChange: (String) -> Unit = {}

    init {
        compositeDisposable += binding.ivClear.onClick(0) {
            binding.etSearch.setText("")
        }
        compositeDisposable += binding.etSearch.onTextChange {
            binding.ivClear.isVisible = it.isNotBlank()
        }
        compositeDisposable += binding.etSearch.onTextChange(500) {
            onTextChange(it)
        }
    }

    fun setTextChangeListener(listener: (String) -> Unit = {}) {
        onTextChange = listener
    }

    fun enabled(enabled: Boolean) {
        binding.etSearch.isEnabled = enabled
    }

}