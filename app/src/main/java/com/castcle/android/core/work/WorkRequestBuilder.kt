package com.castcle.android.core.work

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkRequest

interface WorkRequestBuilder {
    fun buildUpdateProfileAvatarWorkerRequest(filePath: String): WorkRequest
}

class WorkRequestBuilderImpl : WorkRequestBuilder {

    override fun buildUpdateProfileAvatarWorkerRequest(
        filePath: String
    ): WorkRequest {
        return OneTimeWorkRequest.Builder(UpLoadProfileAvatarWorker::class.java)
            .setInputData(UpLoadProfileAvatarWorker.provideInputData(filePath))
            .addTag(ImageUploaderWorkHelper.UPLOAD_IMAGE_TAG)
            .build()
    }
}
