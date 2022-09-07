package com.castcle.android.presentation.sign_up.create_profile.entity

import com.google.gson.annotations.SerializedName

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
//  Created by sklim on 18/10/2021 AD at 12:03.

data class CreatePageRequest(
    var castcleIdRequest: String = "",
    @SerializedName("displayName")
    var displayName: String? = null,
    @SerializedName("castcleId")
    var castcleId: String? = null,
    @SerializedName("overview")
    var overview: String? = null,
    @SerializedName("images")
    var images: ImagesPageRequest? = null,
    @SerializedName("links")
    val links: LinksRequest? = null,
)

data class LinksRequest(
    @SerializedName("facebook")
    val facebook: String? = null,
    @SerializedName("medium")
    val medium: String? = null,
    @SerializedName("twitter")
    val twitter: String? = null,
    @SerializedName("website")
    val website: String? = null,
    @SerializedName("youtube")
    val youtube: String? = null
)

data class ImagesPageRequest(
    @SerializedName("avatar")
    val avatar: String = "",
    @SerializedName("cover")
    val cover: String = ""
)
