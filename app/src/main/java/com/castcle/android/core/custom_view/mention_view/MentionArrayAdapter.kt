package com.castcle.android.core.custom_view.mention_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.castcle.android.core.extensions.loadAvatarImage
import com.castcle.android.databinding.ItemMentionUserBinding
import com.castcle.android.domain.user.entity.UserEntity

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 15/9/2021 AD at 11:12.

class MentionArrayAdapter(
    context: Context,
    val items: MutableList<MentionsItem> = mutableListOf()
) : ArrayAdapter<MentionArrayAdapter.MentionsItem>(context, 0, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewBinding = ItemMentionUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        if (items.isNotEmpty()) {
            onBindHolder(items[position], viewBinding)
        }
        return viewBinding.root
    }

    override fun getItem(position: Int) = items[position]

    override fun getCount() = items.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewBinding = ItemMentionUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, true
        )
        if (items.isNotEmpty()) {
            onBindHolder(items[position], viewBinding)
        }
        return viewBinding.root
    }

    override fun addAll(collection: MutableCollection<out MentionsItem>) {
        super.addAll(collection)
    }

    @SuppressLint("SetTextI18n")
    private fun onBindHolder(
        itemMention: MentionsItem,
        viewBinding: ItemMentionUserBinding
    ) {
        with(viewBinding) {
            ivAvatar.loadAvatarImage(itemMention.user.avatar.thumbnail)
            tvName.text = itemMention.user.displayName
            tvCastcleId.text = itemMention.user.castcleId
            tvFollowed.isVisible = itemMention.user.followed
            ivFollowed.isVisible = itemMention.user.followed
        }
    }

    data class MentionsItem(val user: UserEntity = UserEntity()) {
        override fun toString() = user.castcleId
    }

}
