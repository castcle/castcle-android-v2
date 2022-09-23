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

package com.castcle.android.presentation.setting.ads.boost_ads.item_budget

import android.annotation.SuppressLint
import android.view.inputmethod.EditorInfo
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemEstimatedDailyBinding
import com.castcle.android.presentation.setting.ads.boost_ads.BoostAdsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ItemBudgetViewHolder(
    private val binding: ItemEstimatedDailyBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: BoostAdsListener,
) : CastcleViewHolder<ItemBudgetViewEntity>(binding.root) {

    override var item = ItemBudgetViewEntity()

    init {
        binding.slDailyBudget.addOnChangeListener { _, value, _ ->
            onBindDailyBudGet(value.toDouble())
        }
        binding.slDuration.addOnChangeListener { _, value, _ ->
            onBindDurationBudGet(value.toInt())
        }

        compositeDisposable += binding.tvDailyBugGetValue.onClick {
            listener.onFocusDaily()
            handleInputDailyValue(true, item.dailyBudget)
        }

        binding.etDailyBugGetValue.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onBindDailyBudGet(item.dailyBudget)
                handleInputDailyValue(dailyBudget = item.dailyBudget)
                listener.onHideKeyboard(itemView)
                binding.etDailyBugGetValue.clearFocus()
                false
            } else {
                false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onBindDailyBudGet(value: Double?) {
        if (value != null && value < MIN_DAILY_BUDGET) {
            listener.onLimitDailyBudget()
        } else {
            with(binding) {
                item.dailyBudget = value ?: 0.0
                tvDailyBugGetValue.text = "$ ${value?.toInt()}"
                etDailyBugGetValue.setText(value?.toInt().toString())
                slDailyBudget.value = value?.toFloat() ?: DEFAULT_VALUE
            }
        }
    }

    private fun onBindDurationBudGet(value: Int?) {
        with(binding) {
            item.durationDay = value ?: 0
            tvDurationValue.text = string(
                R.string.ad_duration_value
            ).format(value)
            slDuration.value = value?.toFloat() ?: DEFAULT_VALUE
        }
    }

    override fun bind(bindItem: ItemBudgetViewEntity) {
        onBindDailyBudGet(item.dailyBudget)
        onBindDurationBudGet(item.durationDay)
    }

    private fun handleInputDailyValue(show: Boolean = false, dailyBudget: Double) {
        with(binding) {
            tvDailyBugGetValue.visibleOrInvisible(!show)
            with(etDailyBugGetValue) {
                filters = arrayOf(InputFilterMax(1, 100))
                visibleOrInvisible(show)
                if (show) {
                    listener.onShowKeyboard(itemView)
                    setText(dailyBudget.toInt().toString())
                    setSelection(dailyBudget.toInt().toString().length)
                    requestFocus()
                }
                compositeDisposable += onTextChange {
                    item.dailyBudget = it.ifBlank {
                        "${item.dailyBudget}"
                    }.toDouble()
                }
            }
        }
    }
}

private const val DEFAULT_VALUE = 10f
private const val MIN_DAILY_BUDGET = 5.0