package com.castcle.android.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.core.net.toUri
import com.castcle.android.presentation.sign_up.update_profile.entity.UpLoadImagesRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

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
//  Created by sklim on 11/3/2022 AD at 09:37.
fun scaleImage(context: Context, image: UpLoadImagesRequest): Flow<UpLoadImagesRequest> {
    return flow {
        emit(
            image.apply {
                avatar = image.avatar?.toUri()?.scaleAndCompressImageFile(context)
                cover = image.cover?.toUri()?.scaleAndCompressImageFile(context)
            }
        )
    }
}

fun Uri?.scaleAndCompressImageFile(context: Context): String? {
    if (this == null) {
        return null
    }
    val bitmap = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, this))
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }

    val tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, context.cacheDir)
    bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, tempFile.outputStream())

    return Base64.encodeToString(tempFile.readBytes(), Base64.NO_WRAP)
}

const val TEMP_FILE_PREFIX = "scaled_"
const val TEMP_FILE_SUFFIX = ".jpeg"
const val JPEG_QUALITY = 90
