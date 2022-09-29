package com.castcle.android.core.extensions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.drjacky.imagepicker.ImagePicker
import com.permissionx.guolindev.PermissionX

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
//  Created by sklim on 7/9/2022 AD at 21:42.

fun Fragment.checkPermissionCamera(onGrant: () -> Unit) {
    PermissionX.init(this)
        .permissions(Manifest.permission.CAMERA)
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                "Core fundamental are based on these permissions",
                "OK",
                "Cancel"
            )
        }
        .request { allGranted, _, deniedList ->
            if (allGranted) {
                onGrant()
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "These permissions are denied: $deniedList",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
}

fun Activity.getIntentImagePicker(
    cameraOrGallery: Int = 0,
    avatarOrCover: Boolean = true
): Intent {
    return when (cameraOrGallery) {
        CAMERA -> if (avatarOrCover) {
            ImagePicker.with(this)
                .cameraOnly()
                .cropOval()
                .cropSquare()
                .createIntent()
        } else {
            ImagePicker.with(this)
                .cameraOnly()
                .cropSquare()
                .createIntent()
        }
        GALLERY -> if (avatarOrCover) {
            ImagePicker.with(this)
                .galleryOnly()
                .cropOval()
                .cropSquare()
                .createIntent()
        } else {
            ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .createIntent()
        }
        else -> ImagePicker.with(this)
            .galleryOnly()
            .cropSquare()
            .createIntent()
    }
}

const val CAMERA = 0
const val GALLERY = 1