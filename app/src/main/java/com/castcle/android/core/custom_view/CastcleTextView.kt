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

package com.castcle.android.core.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ReplacementSpan
import android.util.AttributeSet
import android.util.Patterns
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.doOnAttach
import androidx.core.view.isInvisible
import androidx.core.widget.TextViewCompat
import com.castcle.android.R
import com.castcle.android.core.extensions.copyToClipboard
import java.util.regex.*
import kotlin.math.roundToInt

class CastcleTextView : AppCompatTextView, PopupMenu.OnMenuItemClickListener {

    private var hashtagColor = Color.BLUE
    private var mentionColor = Color.YELLOW
    private var emailColor = Color.CYAN
    private var urlColor = Color.RED
    private var phoneColor = Color.GREEN
    private var normalTextColor = Color.BLACK
    private var selectedColor = Color.GRAY
    private var isUnderline = false
    private var linkedType: Int = 0
    private var linkedMentions: List<String> = ArrayList()
    private var linkedHashtag: List<String> = ArrayList()
    private var highlightText: List<String> = ArrayList()
    private var isLinkedMention = false
    private var highlightRadius: Int = 8
    private var highlightBackgroundColor = Color.YELLOW
    private var highlightTextColor = Color.BLACK


    private var patternHashtag: Pattern? = null
    private var patternMention: Pattern? = null
    private var patternText: Pattern? = null
    private var patternLink: Pattern? = null

    private var onLinkClickListener: LinkClickListener? = null

    var state: State = State.COLLAPSED
        private set(value) {
            field = value
            text = when (value) {
                State.EXPANDED -> originalText
                State.COLLAPSED -> collapseText
                State.NON_EXPANDED -> originalText
            }
            changeListener?.onStateChange(value)
        }

    private var _lineEnd: Int = 0

    var changeListener: ChangeListener? = null

    val isExpanded
        get() = state == State.EXPANDED

    val isCollapsed
        get() = state == State.COLLAPSED

    private var originalText: CharSequence = ""
    private var collapseText: CharSequence = ""

    fun onClearMessage() {
        originalText = ""
        collapseText = ""
    }

    private val readMoreText = context.getString(R.string.read_more)

    interface LinkClickListener {
        fun onLinkClicked(linkType: LinkedType, matchedText: String)
    }

    fun setLinkClickListener(onLinkClickListener: LinkClickListener) {
        this.onLinkClickListener = onLinkClickListener
    }

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @SuppressLint("CheckResult")
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        setLineSpacing(lineSpacingExtra, 1.25F)
        TextViewCompat.setTextAppearance(this, R.style.AppTextSarabunLight_Small)
        movementMethod = LinkedMovement.getInstance()
        highlightColor = ContextCompat.getColor(context, R.color.blue_transparent)
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SocialTextView, defStyleAttr, defStyleAttr
        )

        hashtagColor = typedArray.getColor(
            R.styleable.SocialTextView_hashtagColor,
            Color.parseColor("#82B1FF")
        )
        mentionColor = typedArray.getColor(
            R.styleable.SocialTextView_mentionColor,
            Color.parseColor("#BCBCCF")
        )
        emailColor =
            typedArray.getColor(R.styleable.SocialTextView_emailColor, Color.parseColor("#FF9E80"))
        urlColor =
            typedArray.getColor(
                R.styleable.SocialTextView_urlColor,
                ContextCompat.getColor(context, R.color.blue)
            )
        phoneColor =
            typedArray.getColor(R.styleable.SocialTextView_phoneColor, Color.parseColor("#03A9F4"))
        normalTextColor =
            typedArray.getColor(R.styleable.SocialTextView_normalTextColor, Color.WHITE)
        selectedColor = typedArray.getColor(R.styleable.SocialTextView_selectedColor, Color.GRAY)
        isUnderline = typedArray.getBoolean(R.styleable.SocialTextView_underLine, false)

        //highlight attributes
        highlightRadius = typedArray.getInt(R.styleable.SocialTextView_highlightRadius, 8)
        highlightBackgroundColor =
            typedArray.getColor(R.styleable.SocialTextView_highlightColor, Color.YELLOW)
        highlightTextColor =
            typedArray.getColor(R.styleable.SocialTextView_highlightTextColor, Color.BLACK)


        linkedType =
            (typedArray.getInt(R.styleable.SocialTextView_linkTypes, LinkedType.TEXT.value))

        if (typedArray.hasValue(R.styleable.SocialTextView_android_text)) {
            setLinkText(typedArray.getText(R.styleable.SocialTextView_android_text))
        }
        setOnLongClickListener {
            showPopup(this)
            true
        }
        typedArray.recycle()
    }

    fun toggle() {
        when (state) {
            State.EXPANDED -> collapse()
            State.COLLAPSED -> expand()
            else -> {}
        }
    }

    private fun collapse() {
        if (isCollapsed || collapseText.isEmpty()) {
            return
        }
        state = State.COLLAPSED
    }

    private fun expand() {
        if (isExpanded || originalText.isEmpty()) {
            return
        }
        state = State.EXPANDED
    }

    fun setTextReadMore(message: CharSequence?) {
        doOnAttach {
            post { setupReadMore(message) }
        }
    }

    fun setCollapseText(message: String): CharSequence {
        originalText = addSocialMediaSpan(message)
        if (message.length < anchorPoint) {
            return originalText.also {
                text = it
            }
        }
        return buildSpannedString {
            append(addSocialMediaSpan(getTrimmedText(message)))
            color(urlColor) { append(readMoreText) }
        }.also {
            state = State.COLLAPSED
            text = it
            collapseText = it
        }
    }

    private fun getTrimmedText(message: String): CharSequence {
        return message.subSequence(0, anchorPoint)
    }

    private fun needSkipSetupReadMore(): Boolean =
        isInvisible || lineCount <= DEFAULT_MAX_LINE ||
            text == null || text == collapseText

    private fun setupReadMore(message: CharSequence?) {
        if (needSkipSetupReadMore()) {
            appendLinkText(message.toString())
            originalText = ""
            collapseText = ""
            return
        }
        originalText = addSocialMediaSpan(message)

        val adjustCutCount = getAdjustCutCount(DEFAULT_MAX_LINE, readMoreText)
        val maxTextIndex = layout.getLineVisibleEnd(DEFAULT_MAX_LINE - 1)
        val originalSubText = text.substring(0, maxTextIndex - 1 - adjustCutCount)

        collapseText = buildSpannedString {
            append(addSocialMediaSpan(originalSubText))
            color(urlColor) { append(readMoreText) }
        }

        text = collapseText
    }

    override fun performLongClick(): Boolean {
        return try {
            super.performLongClick()
        } catch (e: NullPointerException) {
            true
        }
    }

    override fun performLongClick(x: Float, y: Float): Boolean {
        return try {
            super.performLongClick(x, y)
        } catch (e: NullPointerException) {
            true
        }
    }

    private fun setLinkText(text: CharSequence?) {
        setText(addSocialMediaSpan(text))
    }

    private fun getAdjustCutCount(maxLine: Int, readMoreText: String): Int {
        val lastLineStartIndex = layout.getLineVisibleEnd(maxLine - 2) + 1
        val lastLineEndIndex = layout.getLineVisibleEnd(maxLine - 1)
        val lastLineText = text.substring(lastLineStartIndex, lastLineEndIndex)

        val bounds = Rect()
        paint.getTextBounds(lastLineText, 0, lastLineText.length, bounds)

        var adjustCutCount = -1
        do {
            adjustCutCount++
            val subText = lastLineText.substring(0, lastLineText.length - adjustCutCount)
            val replacedText = subText + readMoreText
            paint.getTextBounds(replacedText, 0, replacedText.length, bounds)
            val replacedTextWidth = bounds.width()
        } while (replacedTextWidth > width)

        return adjustCutCount
    }

    enum class State {
        EXPANDED, COLLAPSED, NON_EXPANDED
    }

    fun setHighlightText(highlightText: List<String>) {
        this.highlightText = highlightText
        val temp = text.toString()
        text = ""
        text = addSocialMediaSpan(temp)
    }

    fun setLinkedMention(linkedMentions: List<String>) {
        this.linkedMentions = linkedMentions
        isLinkedMention = true
        val temp = text.toString()
        text = ""
        text = addSocialMediaSpan(temp)
    }

    fun setLinkedHashtag(linkedHashtag: List<String>) {
        this.linkedHashtag = linkedHashtag
        val temp = text.toString()
        text = ""
        text = addSocialMediaSpan(temp)
    }

    fun appendLinkText(text: String) {
        this.text = ""
        append(addSocialMediaSpan(text))
    }

    private fun addSocialMediaSpan(text: CharSequence?): SpannableString {

        val items = collectLinkItemsFromText(text.toString())
        val textSpan = SpannableString(text)
        for (item in items) {
            if (item.mode == LinkedType.HIGHLITH.value) {
                textSpan.setSpan(
                    RoundedHighlightSpan(
                        highlightRadius,
                        highlightBackgroundColor,
                        highlightTextColor
                    ),
                    item.start,
                    item.end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                textSpan.setSpan(object :
                    TouchableSpan(
                        getColorByMode(LinkedType.getType(item.mode)),
                        selectedColor,
                        isUnderline
                    ) {
                    override fun onClick(view: View) {
                        //super.onClick(view)
                        spanTextCanClick(item.mode, enableClick = {
                            onLinkClickListener?.onLinkClicked(
                                LinkedType.getType(item.mode),
                                item.matched.trim()
                            )
                        })
                    }
                }, item.start, item.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return textSpan
    }

    private fun getColorByMode(type: LinkedType): Int = when (type) {
        LinkedType.HASHTAG -> hashtagColor
        LinkedType.MENTION -> mentionColor
        LinkedType.PHONE -> phoneColor
        LinkedType.URL -> urlColor
        LinkedType.EMAIL -> emailColor
        LinkedType.TEXT -> normalTextColor

        else -> throw IllegalArgumentException("Invalid Linked Type!")
    }

    private fun spanTextCanClick(itemMode: Int, enableClick: () -> Unit) {
        if (itemMode and LinkedType.EMAIL.value == LinkedType.EMAIL.value) {
            enableClick.invoke()
            return
        }

        if (itemMode and LinkedType.URL.value == LinkedType.URL.value) {
            enableClick.invoke()
            return
        }

        if (itemMode and LinkedType.HASHTAG.value == LinkedType.HASHTAG.value) {
            enableClick.invoke()
            return
        }
        if (itemMode and LinkedType.MENTION.value == LinkedType.MENTION.value) {
            enableClick.invoke()
            return
        }

        if (itemMode and LinkedType.PHONE.value == LinkedType.PHONE.value) {
            enableClick.invoke()
            return
        }

        if (itemMode and LinkedType.HIGHLITH.value == LinkedType.HIGHLITH.value) {
            enableClick.invoke()
            return
        }
    }

    private fun collectLinkItemsFromText(text: String): Set<LinkItem> {
        val items = HashSet<LinkItem>()
        var linkedText: String = text

        if (linkedType and LinkedType.EMAIL.value == LinkedType.EMAIL.value) {
            linkedText = collectLinkItems(
                LinkedType.EMAIL.value,
                items,
                Patterns.EMAIL_ADDRESS.matcher(linkedText),
                linkedText
            )
        }

        if (linkedType and LinkedType.URL.value == LinkedType.URL.value) {
            linkedText =
                collectLinkItems(
                    LinkedType.URL.value,
                    items,
                    linkPattern!!.matcher(linkedText),
                    linkedText
                )
        }

        if (linkedType and LinkedType.HASHTAG.value == LinkedType.HASHTAG.value) {
            linkedText = collectLinkItems(
                LinkedType.HASHTAG.value,
                items,
                hashtagPattern.matcher(linkedText),
                linkedText
            )
        }
        if (linkedType and LinkedType.MENTION.value == LinkedType.MENTION.value) {
            linkedText = collectLinkItems(
                LinkedType.MENTION.value,
                items,
                mentionPattern!!.matcher(linkedText),
                linkedText
            )
        }

        if (linkedType and LinkedType.PHONE.value == LinkedType.PHONE.value) {
            linkedText =
                collectLinkItems(
                    LinkedType.PHONE.value,
                    items,
                    Patterns.PHONE.matcher(linkedText),
                    linkedText
                )
        }
        if (linkedType and LinkedType.HIGHLITH.value == LinkedType.HIGHLITH.value) {
            for (item in highlightText) {
                val start = linkedText.indexOf(item)
                items.add(
                    LinkItem(item, start, start + item.length, LinkedType.HIGHLITH.value)
                )
            }
        }

        collectLinkItems(
            LinkedType.TEXT.value,
            items,
            standartText!!.matcher(linkedText),
            linkedText
        )


        return items
    }

    private val standartText: Pattern?
        get() {
            if (patternText == null) {
                patternText = Pattern.compile("(?u)(?<![@])#?\\b\\w\\w+\\b")
            }
            return patternText
        }

    private val linkPattern: Pattern?
        get() {
            if (patternLink == null) {
                patternLink = Pattern.compile(
                    "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnrwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eouw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)"
                )
            }
            return patternLink
        }

    private val mentionPattern: Pattern?
        get() {
            if (patternMention == null) {
                patternMention = Pattern.compile("(?:^|\\s|$|[.])@[\\p{L}0-9_]*")
            }
            return patternMention
        }

    private val hashtagPattern: Pattern
        get() {
            if (patternHashtag == null) {
                patternHashtag = Pattern.compile("#(\\w+)[\\p{Alnum}](?:^|\\s|\$)*")
            }
            return patternHashtag!!
        }

    private fun collectLinkItems(
        mode: Int, items: MutableCollection<LinkItem>,
        matcher: Matcher,
        text: String
    ): String {
        var text = text
        while (matcher.find()) {
            var matcherStart = matcher.start()
            var matchedText = matcher.group()

            if (matchedText.startsWith("/n")) {
                matcherStart += 1
                matchedText = matchedText.substring(1)
            }

            when {
                mode == LinkedType.HASHTAG.value && linkedHashtag.isNotEmpty() -> {
                    if (linkedHashtag.contains(matchedText)) {
                        items.add(
                            LinkItem(matchedText, matcherStart, matcher.end(), mode)
                        )
                    }
                }
                mode == LinkedType.MENTION.value && linkedMentions.isNotEmpty() -> {
                    if (linkedMentions.contains(matchedText)) {
                        items.add(
                            LinkItem(matchedText, matcherStart, matcher.end(), mode)
                        )
                    }
                }
                else -> {
                    items.add(
                        LinkItem(matchedText, matcherStart, matcher.end(), mode)
                    )
                }
            }
            text = text.replace(matchedText, addSpace(matchedText.length - 1))
        }
        return text
    }

    private fun addSpace(count: Int): String {
        var addSpace = " "
        for (i in 0 until count) {
            addSpace = "$addSpace "
        }
        return addSpace
    }

    interface ChangeListener {
        fun onStateChange(state: State)
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_copy_message, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }

    override fun onMenuItemClick(menu: MenuItem?): Boolean {
        return when (menu?.itemId) {
            R.id.copyMessage -> {
                context.copyToClipboard(text.toString())
                true
            }
            else -> false
        }
    }
}

enum class LinkedType(val value: Int) {
    TEXT(0),
    HASHTAG(2),
    MENTION(4),
    URL(8),
    EMAIL(16),
    PHONE(32),
    HIGHLITH(64);

    companion object {
        fun getType(value: Int) = values().firstOrNull() { it.value == value } ?: TEXT
    }


}

private const val DEFAULT_MAX_LINE = 4
private const val anchorPoint = 300

class LinkedMovement : LinkMovementMethod() {
    private val touchBounds = RectF()
    private var pressedSpan: TouchableSpan? = null

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressedSpan = getTouchedSpan(widget, buffer, event)
                if (pressedSpan != null) {
                    pressedSpan!!.setPressed(true)
                    Selection.setSelection(
                        buffer,
                        buffer.getSpanStart(pressedSpan),
                        buffer.getSpanEnd(pressedSpan)
                    )
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val pressedSpan2 = getTouchedSpan(widget, buffer, event)
                if (pressedSpan != null && pressedSpan2 !== pressedSpan) {
                    pressedSpan!!.setPressed(false)
                    pressedSpan = null
                    Selection.removeSelection(buffer)
                }
            }

            else -> {
                if (pressedSpan != null) {
                    pressedSpan!!.setPressed(false)
                    super.onTouchEvent(widget, buffer, event)
                }
                pressedSpan = null
                Selection.removeSelection(buffer)
            }
        }
        return true
    }


    private fun getTouchedSpan(tv: TextView, span: Spannable, e: MotionEvent): TouchableSpan? {
        var x = e.x.toInt()
        var y = e.y.toInt()

        // Ignore padding
        x -= tv.totalPaddingLeft
        y -= tv.totalPaddingTop

        x += tv.scrollX
        y += tv.scrollY

        val layout = tv.layout
        val touchedLine = layout.getLineForVertical(y)
        val touchOffset = layout.getOffsetForHorizontal(touchedLine, x.toFloat())

        touchBounds.left = layout.getLineLeft(touchedLine)
        touchBounds.top = layout.getLineTop(touchedLine).toFloat()
        touchBounds.right = layout.getLineRight(touchedLine)
        touchBounds.bottom = layout.getLineBottom(touchedLine).toFloat()

        var touchSpan: TouchableSpan? = null
        if (touchBounds.contains(x.toFloat(), y.toFloat())) {
            val spans = span.getSpans(touchOffset, touchOffset, TouchableSpan::class.java)
            touchSpan = if (spans.isNotEmpty()) spans[0] else null
        }

        return touchSpan
    }

    companion object {
        private var instance: LinkedMovement? = null

        @Synchronized
        fun getInstance(): LinkedMovement {
            if (instance == null) {
                instance = LinkedMovement()
            }
            return instance as LinkedMovement
        }
    }

}

abstract class TouchableSpan(
    private val normalTextColor: Int,
    private val pressedTextColor: Int,
    private val isUnderline: Boolean
) : ClickableSpan() {

    private var isPressed: Boolean = false


    override fun updateDrawState(paint: TextPaint) {
        super.updateDrawState(paint)
        /*     if (normalTextColor == Color.BLACK && !isUnderline)
                 return
             */
        val textColor = if (isPressed) pressedTextColor else normalTextColor
        paint.color = textColor
        paint.isUnderlineText = isUnderline
        paint.bgColor = Color.TRANSPARENT

    }

    internal fun setPressed(pressed: Boolean) {
        this.isPressed = pressed
    }
}

class RoundedHighlightSpan(
    private val HIGHLIGHT_RADIUS: Int = 8,
    @ColorInt
    private val backgroundColor: Int = Color.YELLOW,
    @ColorInt
    private val textColor: Int = Color.BLACK
) : ReplacementSpan() {


    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fontMetrics: Paint.FontMetricsInt?
    ): Int {
        return paint.measureText(text, start, end).roundToInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        left: Float,
        top: Int,
        right: Int,
        bottom: Int,
        paint: Paint
    ) {
        val rectF = RectF(
            left,
            top.toFloat(),
            left + measureTextPaint(paint, text, start, end),
            bottom.toFloat()
        )
        paint.color = backgroundColor
        canvas.drawRoundRect(rectF, HIGHLIGHT_RADIUS.toFloat(), HIGHLIGHT_RADIUS.toFloat(), paint)
        paint.color = textColor
        canvas.drawText(text!!, start, end, left, right.toFloat(), paint)
    }


    private fun measureTextPaint(
        paint: Paint,
        charSequence: CharSequence?,
        start: Int,
        end: Int
    ): Float {
        return paint.measureText(charSequence, start, end)
    }

}

data class LinkItem(val matched: String, val start: Int, val end: Int, val mode: Int)