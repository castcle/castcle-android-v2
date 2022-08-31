package com.castcle.android.core.custom_view.mention_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.text.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.castcle.android.R
import com.castcle.android.domain.search.entity.HashtagEntity
import com.castcle.android.domain.user.entity.UserEntity
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
//  Created by sklim on 14/9/2021 AD at 14:54.

class MentionEditText(
    context: Context,
    attrs: AttributeSet
) : AppCompatMultiAutoCompleteTextView(context, attrs), MentionView {

    private val mentionHelper by lazy { MentionHelper(context, this, attrs) }

    init {
        initMentionEditText()
    }

    private val hashtagAdapter by lazy { HashtagArrayAdapter(context) }

    private val mentionAdapter by lazy { MentionArrayAdapter(context) }

    private val textWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun afterTextChanged(s: Editable) = Unit
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && start < s.length) {
                    when (s[start]) {
                        '#' -> if (adapter != hashtagAdapter) setAdapter(hashtagAdapter)
                        '@' -> if (adapter != mentionAdapter) setAdapter(mentionAdapter)
                    }
                }
            }
        }

    @SuppressLint("SetTextI18n")
    private fun initMentionEditText() {
        threshold = 0
        setTokenizer(CharTokenizer())
        addTextChangedListener(textWatcher)
    }

    override fun enoughToFilter() = true

    fun updateHashtagItems(items: List<HashtagEntity>) {
        hashtagAdapter.items.clear()
        hashtagAdapter.items.addAll(items.map { HashtagArrayAdapter.HashtagsItem(it) })
        hashtagAdapter.notifyDataSetChanged()
        if (text.lastOrNull() == '#' && !isPopupShowing) showDropDown()
    }

    fun updateMentionsItems(items: List<UserEntity>) {
        mentionAdapter.items.clear()
        mentionAdapter.items.addAll(items.map { MentionArrayAdapter.MentionsItem(it) })
        mentionAdapter.notifyDataSetChanged()
        if (text.lastOrNull() == '@' && !isPopupShowing) showDropDown()
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

    override fun isHashtagEnabled() = true

    override fun isMentionEnabled() = true

    override fun isHyperlinkEnabled() = true

    override fun setHashtagEnabled(enabled: Boolean) {
        mentionHelper.setHashtagEnabled(enabled)
        setTokenizer(CharTokenizer())
    }

    override fun setMentionEnabled(enabled: Boolean) {
        mentionHelper.setMentionEnabled(enabled)
        setTokenizer(CharTokenizer())
    }

    override fun setHyperlinkEnabled(enabled: Boolean) {
        mentionHelper.setHashtagEnabled(enabled)
    }

    override fun getHashtagColors(): Int {
        return context.getColor(R.color.blue)
    }

    override fun getMentionColors(): Int {
        return context.getColor(R.color.blue)
    }

    override fun getHyperlinkColors(): Int {
        return context.getColor(R.color.blue)
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

    inner class CharTokenizer : Tokenizer {
        private val chars: MutableCollection<Char> = ArrayList()
        override fun findTokenStart(text: CharSequence, cursor: Int): Int {
            var i = cursor
            while (i > 0 && !chars.contains(text[i - 1])) {
                i--
            }
            while (i < cursor && text[i] == ' ') {
                i++
            }

            // imperfect fix for dropdown still showing without symbol found
            if (i == 0 && isPopupShowing) {
                dismissDropDown()
            }
            return i
        }

        override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
            var i = cursor
            val len = text.length
            while (i < len) {
                when {
                    chars.contains(text[i]) -> {
                        return i
                    }
                    else -> {
                        i++
                    }
                }
            }
            return len
        }

        override fun terminateToken(text: CharSequence): CharSequence {
            var i = text.length
            while (i > 0 && text[i - 1] == ' ') {
                i--
            }
            return if (i > 0 && chars.contains(text[i - 1])) {
                text
            } else {
                when (text) {
                    is Spanned -> {
                        val sp: Spannable = SpannableString("$text ")
                        TextUtils.copySpansFrom(
                            text, 0, text.length,
                            Any::class.java, sp, 0
                        )
                        sp
                    }
                    else -> {
                        "$text "
                    }
                }
            }
        }

        init {
            chars.add('#')
            chars.add('@')
        }
    }

    private var parentRect = Rect()

    override fun getFocusedRect(r: Rect?) {
        super.getFocusedRect(r)
        getCustomLayout()?.let { view ->
            view.getFocusedRect(parentRect)
            r?.bottom = parentRect.bottom
        }
    }

    override fun getGlobalVisibleRect(r: Rect?, globalOffset: Point?): Boolean {
        val result = super.getGlobalVisibleRect(r, globalOffset)
        getCustomLayout()?.let { view ->
            view.getGlobalVisibleRect(parentRect, globalOffset)
            r?.bottom = parentRect.bottom
        }
        return result
    }

    private fun getCustomLayout(): ConstraintLayout? {
        var viewParent = parent
        while (viewParent is View) {
            if (viewParent is ConstraintLayout && viewParent.id == R.id.clComment) {
                return viewParent
            }
            viewParent = (viewParent as View).parent
        }
        return null
    }

    override fun requestRectangleOnScreen(rectangle: Rect?): Boolean {
        val result = super.requestRectangleOnScreen(rectangle)
        getCustomLayout()?.let { view ->
            parentRect.set(
                0,
                0,
                view.width,
                view.height
            )
            view.requestRectangleOnScreen(parentRect, true)
        }
        return result
    }

}
