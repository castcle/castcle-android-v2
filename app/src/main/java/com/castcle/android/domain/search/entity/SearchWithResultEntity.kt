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

package com.castcle.android.domain.search.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity

data class SearchWithResultEntity(
    @Embedded val search: SearchEntity = SearchEntity(),
    @Relation(parentColumn = "${TABLE_SEARCH}_originalCastId", entityColumn = "${TABLE_CAST}_id")
    val originalCast: CastEntity? = null,
    @Relation(parentColumn = "${TABLE_SEARCH}_originalUserId", entityColumn = "${TABLE_USER}_id")
    val originalUser: UserEntity? = null,
    @Relation(parentColumn = "${TABLE_SEARCH}_referenceCastId", entityColumn = "${TABLE_CAST}_id")
    val referenceCast: CastEntity? = null,
    @Relation(parentColumn = "${TABLE_SEARCH}_referenceUserId", entityColumn = "${TABLE_USER}_id")
    val referenceUser: UserEntity? = null,
)