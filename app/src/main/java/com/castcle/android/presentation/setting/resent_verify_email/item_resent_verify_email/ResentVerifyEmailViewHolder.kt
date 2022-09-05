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

package com.castcle.android.presentation.setting.resent_verify_email.item_resent_verify_email

import android.text.Html
import android.widget.TextView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemResentVerifyEmailBinding
import com.castcle.android.presentation.setting.resent_verify_email.ResentVerifyEmailListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ResentVerifyEmailViewHolder(
    private val binding: ItemResentVerifyEmailBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ResentVerifyEmailListener,
) : CastcleViewHolder<ResentVerifyEmailViewEntity>(binding.root) {

    override var item = ResentVerifyEmailViewEntity()

    init {
        compositeDisposable += binding.tvResendEmail.onClick {
            listener.onResentVerifyEmailClicked()
        }
    }

    override fun bind(bindItem: ResentVerifyEmailViewEntity) {
        val text = context().getString(
            R.string.fragment_resent_verify_email_title_3,
            item.email,
        )
        binding.tvTitle3.setText(
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY),
            TextView.BufferType.SPANNABLE,
        )
    }

}