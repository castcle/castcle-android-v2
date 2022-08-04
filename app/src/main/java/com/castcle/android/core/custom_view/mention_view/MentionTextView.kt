package com.castcle.android.core.custom_view.mention_view

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.regex.*

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
//  Created by sklim on 6/10/2021 AD at 17:27.

class MentionTextView(
    context: Context,
    attrs: AttributeSet
) : AppCompatTextView(context, attrs), MentionView {

    private lateinit var mentionHelper: MentionHelper

    init {
        initMentionTextView(context, attrs)
    }

    private fun initMentionTextView(context: Context, attrs: AttributeSet) {
        mentionHelper = MentionHelper(context, this, attrs)
    }

    override fun getHashtagPattern(): Pattern {
        return mentionHelper.getHashtagPattern()
    }

    override fun getMentionPattern(): Pattern {
        return mentionHelper.getMentionPattern()
    }

    override fun getHyperlinkPattern(): Pattern {
        return mentionHelper.getHyperlinkPattern()
    }

    override fun setHashtagPattern(pattern: Pattern) {
        mentionHelper.setHashtagPattern(pattern)
    }

    override fun setMentionPattern(pattern: Pattern) {
        mentionHelper.setMentionPattern(pattern)
    }

    override fun setHyperlinkPattern(pattern: Pattern) {
        mentionHelper.setHyperlinkPattern(pattern)
    }

    override fun isHashtagEnabled(): Boolean {
        return mentionHelper.isHashtagEnabled()
    }

    override fun isMentionEnabled(): Boolean {
        return mentionHelper.isMentionEnabled()
    }

    override fun isHyperlinkEnabled(): Boolean {
        return mentionHelper.isHyperlinkEnabled()
    }

    override fun setHashtagEnabled(enabled: Boolean) {
        mentionHelper.setHashtagEnabled(enabled)
    }

    override fun setMentionEnabled(enabled: Boolean) {
        mentionHelper.setMentionEnabled(enabled)
    }

    override fun setHyperlinkEnabled(enabled: Boolean) {
        movementMethod = LinkMovementMethod.getInstance()
        mentionHelper.setHyperlinkEnabled(enabled)
    }

    override fun getHashtagColors(): Int {
        return mentionHelper.getHashtagColors()
    }

    override fun getMentionColors(): Int {
        return mentionHelper.getMentionColors()
    }

    override fun getHyperlinkColors(): Int {
        return mentionHelper.getHyperlinkColors()
    }

    override fun setHashtagColors(colors: Int) {
        mentionHelper.setHashtagColors(colors)
    }

    override fun setMentionColors(colors: Int) {
        mentionHelper.setMentionColors(colors)
    }

    override fun setHyperlinkColors(colors: Int) {
        mentionHelper.setHyperlinkColors(colors)
    }

    override fun setOnHashtagClickListener(listener: MentionView.OnClickListener) {
        mentionHelper.setOnHashtagClickListener(listener)
    }

    override fun setOnMentionClickListener(listener: MentionView.OnClickListener) {
        mentionHelper.setOnMentionClickListener(listener)
    }

    override fun setOnHyperlinkClickListener(listener: MentionView.OnClickListener) {
        mentionHelper.setOnHyperlinkClickListener(listener)
    }

    override fun setHashtagTextChangedListener(listener: MentionView.OnChangedListener) {
        mentionHelper.setHashtagTextChangedListener(listener)
    }

    override fun setMentionTextChangedListener(listener: MentionView.OnChangedListener) {
        mentionHelper.setMentionTextChangedListener(listener)
    }

    override fun getHashtags(): List<String> {
        return mentionHelper.getHashtags()
    }

    override fun getMentions(): List<String> {
        return mentionHelper.getMentions()
    }

    override fun setMentionSelected(message: String) {
        mentionHelper.setMentionSelected(message)
    }

    override fun getHyperlinks(): List<String> {
        return mentionHelper.getHyperlinks()
    }
}