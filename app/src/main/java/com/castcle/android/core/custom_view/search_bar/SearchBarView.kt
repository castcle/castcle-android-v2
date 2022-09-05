/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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

    private var onTextChangeWithoutDebounce: (String) -> Unit = {}

    private var onSearchClicked: (String) -> Unit = {}

    init {
        compositeDisposable += binding.ivClear.onClick(0) {
            binding.etSearch.setText("")
        }
        compositeDisposable += binding.etSearch.onTextChange {
            binding.ivClear.isVisible = it.isNotBlank()
            onTextChangeWithoutDebounce(it)
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

    fun setTextChangeWithoutDebounceListener(listener: (String) -> Unit = {}) {
        onTextChangeWithoutDebounce = listener
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

    fun setHintText(hint: String) {
        binding.etSearch.hint = hint
    }

    fun setText(text: String) {
        binding.etSearch.setText(text)
    }

}