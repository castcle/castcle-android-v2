package com.castcle.android.core.custom_view.search_bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutSearchBarBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign


class SearchBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = LayoutSearchBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    private var onTextChange: (String) -> Unit = {}

    private var onSearchClicked: (String) -> Unit = {}

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
        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchClicked(binding.etSearch.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun setTextChangeListener(listener: (String) -> Unit = {}) {
        onTextChange = listener
    }

    fun setSearchClickedListener(listener: (String) -> Unit = {}) {
        onSearchClicked = listener
    }

    fun enabled(enabled: Boolean) {
        binding.etSearch.isEnabled = enabled
    }

    fun focus() {
        binding.etSearch.showKeyboard()
    }

    fun setText(text: String) {
        binding.etSearch.setText(text)
    }

}