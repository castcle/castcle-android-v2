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

package com.castcle.android.core.extensions

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(this, 0)
}

class TextChangeCastcleIdListener(
    val editText: EditText,
    val onTextChanged: ((String) -> Unit)? = null
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged?.invoke(handlerInputCastcleId(p0.toString()))
    }

    override fun afterTextChanged(message: Editable?) {
        with(editText) {
            removeTextChangedListener(this@TextChangeCastcleIdListener)
            setText(handlerInputCastcleId(message.toString()))
            addTextChangedListener(this@TextChangeCastcleIdListener)
            setSelection(editText.text.toString().length)
        }
    }
}

fun handlerInputCastcleId(message: String): String {
    return when {
        message.contains(defaultCastcleId) && message.length == 1 -> {
            ""
        }
        message.contains(defaultCastcleId) || message.isEmpty() -> {
            message
        }
        else -> {
            "@$message"
        }
    }
}

class TextChangeListener(val editText: EditText, val onTextChanged: ((String) -> Unit)? = null) :
    TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged?.invoke(handlerInputUrl(p0.toString()))
    }

    override fun afterTextChanged(message: Editable?) {
        editText.removeTextChangedListener(this)
        handlerInputUrl(message.toString()).run {
            editText.setText(this)
            editText.setSelection(this.length)
        }
        editText.addTextChangedListener(this)
    }
}

private fun handlerInputUrl(message: String): String {
    return when {
        message == schemeFailHttps -> ""
        message.contains(schemeHttps) -> message.replaceBeforeLast(schemeHttps, "")
        else -> "$schemeHttps$message"
    }
}

fun Editable.isHasValue(): String? {
    return this.toString().ifBlank {
        null
    }
}

private const val schemeHttps = "https://"
private const val schemeFailHttps = "https:/"
private const val defaultCastcleId = "@"
