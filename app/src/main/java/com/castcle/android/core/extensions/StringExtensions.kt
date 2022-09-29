package com.castcle.android.core.extensions

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
//  Created by sklim on 5/9/2022 AD at 15:03.

fun String.isEmail() = this.matches(EMAIL_PATTERN.toRegex())

fun String.isLowerAndUpperCase() = this.matches(UPPER_AND_LOWER_PATTERN.toRegex())

fun String.isMinCharacters() = this.length >= MIN_CHARACTER_PASSWORD

fun String.isEngText() = this.matches(ENG_PATTERN.toRegex())

private val ENG_PATTERN = Pattern.compile("""^[_A-z0-9]*((\s)*[_A-z0-9])*${'$'}""")

private const val MIN_CHARACTER_PASSWORD = 6

private const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
    "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\." +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25})+"

private val UPPER_AND_LOWER_PATTERN: Pattern = Pattern.compile(
    "^(?=.*[a-z])(?=.*[A-Z])"
        + "(?=\\S+$)"
        + ".{2,}"
        + "$"
)
