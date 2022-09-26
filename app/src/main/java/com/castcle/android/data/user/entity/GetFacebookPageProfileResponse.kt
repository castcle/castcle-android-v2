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

package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GetFacebookPageProfileResponse(
    @SerializedName("data") val data: List<Data>? = null,
) {

    data class Cover(
        @SerializedName("source") val source: String? = null,
    )

    data class Data(
        @SerializedName("about") val about: String? = null,
        @SerializedName("access_token") val access_token: String? = null,
        @SerializedName("cover") val cover: Cover? = null,
        @SerializedName("id") val id: String? = null,
        @SerializedName("link") val link: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("picture") val picture: Picture? = null,
        @SerializedName("username") val username: String? = null,
    )

    data class Picture(
        @SerializedName("data") val data: PictureData? = null,
    )

    data class PictureData(
        @SerializedName("url") val url: String? = null,
    )

}