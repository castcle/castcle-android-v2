package com.castcle.android.core.work

import android.content.Context
import androidx.work.*
import com.castcle.android.core.extensions.scaleImage
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.presentation.sign_up.update_profile.entity.UploadImageRequest
import com.castcle.android.presentation.sign_up.update_profile.entity.toImageRequestModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
//  Created by sklim on 20/10/2021 AD at 10:36.

class UpLoadProfileAvatarWorker(
    private val appContext: Context,
    params: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(appContext, params) {

    @OptIn(FlowPreview::class)
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val updateRequestString = inputData.getString(EXTRA_UPLOAD_AVATAR_PARAM) ?: ""
            val imageUpLoad = updateRequestString.toImageRequestModel()
            scaleImage(context = appContext, imageUpLoad)
                .flatMapConcat {
                    userRepository.updateUserProfile(
                        UploadImageRequest(
                            images = it
                        )
                    )
                }.mapNotNull {
                    when (it) {
                        is BaseUiState.SuccessNonBody -> {
                            val outputData = buildSuccessOutput()
                            Result.success(outputData)
                        }
                        else -> {
                            val outputData = buildErrorOutput(it.apiException)
                            Result.failure(outputData)
                        }
                    }
                }.first()
        }
    }

    private fun buildSuccessOutput(): Data {
        val status = ImageUploaderWorkHelper.Result.SUCCESS.ordinal

        return workDataOf(
            ImageUploaderWorkHelper.EXTRA_UPLOAD_RESULT to status,
        )
    }

    private fun buildErrorOutput(error: Throwable?): Data {
        val status = ImageUploaderWorkHelper.Result.FAILURE.ordinal
        val errorMessageRes = error?.message

        return workDataOf(
            ImageUploaderWorkHelper.EXTRA_UPLOAD_RESULT to status,
            ImageUploaderWorkHelper.EXTRA_UPLOAD_ERROR_RESULT to errorMessageRes
        )
    }

    companion object {
        fun provideInputData(imagePath: String): Data =
            workDataOf(
                EXTRA_UPLOAD_AVATAR_PARAM to imagePath,
            )
    }
}

private const val EXTRA_UPLOAD_AVATAR_PARAM = "EXTRA_UPLOAD_AVATAR_PARAM"
