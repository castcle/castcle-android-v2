package com.castcle.android.presentation.profile.edit_profile

import android.net.Uri
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.sign_up.update_profile.item_update_profile.UpdateProfileViewEntity

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
//  Created by sklim on 25/9/2022 AD at 17:27.

data class ItemEditEntity(
    val userEntity: UserEntity = UserEntity(),
    val avatarUpLoad: Uri? = null,
    var onUploadAvatar: Boolean? = null,
    val coveUpLoad: Uri? = null,
    var onUploadCover: Boolean? = null,
    var castcleId: String? = null,
    var displayName: String? = null,
    var birthDate: String? = null,
    var overview: String = "",
    var linkFacebook: String? = null,
    var linkYoutube: String? = null,
    var linkTwitter: String? = null,
    var linkMedium: String? = null,
    var linkWeb: String? = null,
    override var uniqueId: String = "",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<UpdateProfileViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ItemEditEntity>() == this
    }

    override fun viewType() = 0
}