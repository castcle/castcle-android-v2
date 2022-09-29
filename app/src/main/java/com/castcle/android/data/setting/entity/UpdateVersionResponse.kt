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

package com.castcle.android.data.setting.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateVersionResponse(
    @SerializedName("android") val android: Android? = null,
    @SerializedName("meta") val meta: Meta? = null,
) {

    data class Android(
        @SerializedName("url") val url: String? = null,
        @SerializedName("version") val version: String? = null,
    )

    data class Message(
        @SerializedName("en") val en: String? = null,
    )

    data class Meta(
        @SerializedName("button") val button: Message? = null,
        @SerializedName("button_cancel") val buttonCancel: Message? = null,
        @SerializedName("button_ok") val buttonOk: Message? = null,
        @SerializedName("message") val message: Message? = null,
        @SerializedName("title") val title: Message? = null,
    )

}