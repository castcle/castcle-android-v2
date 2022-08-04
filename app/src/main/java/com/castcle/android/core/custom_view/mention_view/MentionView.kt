package com.castcle.android.core.custom_view.mention_view

import androidx.core.util.PatternsCompat
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
//  Created by sklim on 15/9/2021 AD at 08:15.

interface MentionView {
    /**
     * Returns regex that are responsible for finding **hashtags**.
     * By default, the pattern are `#(\w+)`.
     */

    fun getHashtagPattern(): Pattern

    /**
     * Returns regex that are responsible for finding **mentions**.
     * By default, the pattern are `@(\w+)`.
     */

    fun getMentionPattern(): Pattern

    /**
     * Returns regex that are responsible for finding **hyperlinks**.
     * By default, the pattern are [PatternsCompat.WEB_URL].
     */

    fun getHyperlinkPattern(): Pattern

    /**
     * Modify regex that are responsible for finding **hashtags**.
     *
     * @param pattern custom regex. When null, default pattern will be used.
     */
    fun setHashtagPattern(pattern: Pattern)

    /**
     * Modify regex that are responsible for finding **hashtags**.
     *
     * @param pattern custom regex. When null, default pattern will be used.
     */
    fun setMentionPattern(pattern: Pattern)

    /**
     * Modify regex that are responsible for finding **hashtags**.
     *
     * @param pattern custom regex. When null, default pattern will be used.
     */
    fun setHyperlinkPattern(pattern: Pattern)

    /**
     * Returns true if **hashtags** in this view are spanned.
     */
    fun isHashtagEnabled(): Boolean

    /**
     * Returns true if **mentions** in this view are spanned.
     */
    fun isMentionEnabled(): Boolean

    /**
     * Returns true if **hyperlinks** in this view are spanned.
     */
    fun isHyperlinkEnabled(): Boolean

    /**
     * Determine whether this view should span **hashtags**.
     *
     * @param enabled True when spanning should be enabled.
     */
    fun setHashtagEnabled(enabled: Boolean)

    /**
     * Determine whether this view should span **mentions**.
     *
     * @param enabled True when spanning should be enabled.
     */
    fun setMentionEnabled(enabled: Boolean)

    /**
     * Determine whether this view should span **hyperlinks**.
     *
     * @param enabled True when spanning should be enabled.
     */
    fun setHyperlinkEnabled(enabled: Boolean)

    /**
     * Returns color instance of **hashtags**, default is color accent of current app theme.
     * Will still return corresponding color even when [.isHashtagEnabled] is false.
     */

    fun getHashtagColors(): Int

    /**
     * Returns color instance of **mentions**, default is color accent of current app theme.
     * Will still return corresponding color even when [.isMentionEnabled] ()} is false.
     */

    fun getMentionColors(): Int

    /**
     * Returns color instance of **hyperlinks**, default is color accent of current app theme.
     * Will still return corresponding color even when [.isHyperlinkEnabled] ()} is false.
     */

    fun getHyperlinkColors(): Int

    /**
     * Sets **hashtags** color instance.
     *
     * @param colors Colors state list instance.
     */
    fun setHashtagColors(colors: Int)

    /**
     * Sets **mentions** color instance.
     *
     * @param colors Colors state list instance.
     */
    fun setMentionColors(colors: Int)

    /**
     * Sets **hyperlinks** color instance.
     *
     * @param colors Colors state list instance.
     */
    fun setHyperlinkColors(colors: Int)

    /**
     * Returns color integer of **hashtags**.
     *
     * @see .getHashtagColors
     */

    /**
     * Registers a callback to be invoked when a **hashtag** is clicked.
     *
     * @param listener The callback that will run.
     */
    fun setOnHashtagClickListener(listener: OnClickListener)

    /**
     * Registers a callback to be invoked when a **mention** is clicked.
     *
     * @param listener The callback that will run.
     */
    fun setOnMentionClickListener(listener: OnClickListener)

    fun setOnHyperlinkClickListener(listener: OnClickListener)

    fun setHashtagTextChangedListener(listener: OnChangedListener)

    fun setMentionTextChangedListener(listener: OnChangedListener)

    fun getHashtags(): List<String>

    fun getMentions(): List<String>

    fun setMentionSelected(message: String)

    fun getHyperlinks(): List<String>

    interface OnClickListener {
        fun onClick(view: MentionView, text: CharSequence)
    }

    interface OnChangedListener {
        fun onChanged(view: MentionView, text: CharSequence)
    }
}