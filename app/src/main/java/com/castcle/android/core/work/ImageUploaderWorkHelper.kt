package com.castcle.android.core.work

import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.castcle.android.core.work.ImageUploaderWorkHelper.Companion.EXTRA_UPLOAD_ERROR_RESULT
import com.castcle.android.core.work.ImageUploaderWorkHelper.Companion.EXTRA_UPLOAD_RESULT
import com.castcle.android.core.work.ImageUploaderWorkHelper.Companion.EXTRA_UPLOAD_URL_RESULT
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Singleton

interface ImageUploaderWorkHelper {

    companion object {
        const val UPLOAD_IMAGE_TAG = "UPLOAD_IMAGE_TAG"
        const val EXTRA_UPLOAD_RESULT = "EXTRA_UPLOAD_RESULT"
        const val EXTRA_UPLOAD_URL_RESULT = "EXTRA_UPLOAD_URL_RESULT"
        const val EXTRA_UPLOAD_ERROR_RESULT = "EXTRA_UPLOAD_ERROR_RESULT"
    }

    sealed class Status {
        class Error(@StringRes val error: Int?) : Status()

        object Uploading : Status()

        class Success(val userResponse: String) : Status()

        object LostInternetConnect : Status()
    }

    enum class Result {
        SUCCESS,
        FAILURE
    }

    val uploadStatus: Observable<Status>

    fun uploadImage(filePath: String)
}

class ImageUploaderWorkHelperImpl constructor(
    private val workManager: WorkManager,
    private val workRequestBuilder: WorkRequestBuilder
) : ImageUploaderWorkHelper {

    override val uploadStatus: Observable<ImageUploaderWorkHelper.Status> =
        Observable.create<ImageUploaderWorkHelper.Status> { emitter ->
            val statusesByTagLiveData = workManager.getWorkInfosByTagLiveData(
                ImageUploaderWorkHelper.UPLOAD_IMAGE_TAG
            )
            val observer: Observer<List<WorkInfo>> = Observer { workInfo ->
                workInfo.lastOrNull()?.let { info ->
                    var status: ImageUploaderWorkHelper.Status? = null
                    if (info.state == WorkInfo.State.RUNNING) {
                        status = ImageUploaderWorkHelper.Status.Uploading
                    }
                    if (info.state.isFinished) {
                        val result = ImageUploaderWorkHelper.Result.values()[
                            info.outputData.getInt(
                                EXTRA_UPLOAD_RESULT, ImageUploaderWorkHelper.Result.SUCCESS.ordinal
                            )
                        ]
                        status = when (result) {
                            ImageUploaderWorkHelper.Result.SUCCESS -> {
                                val profileImageUrl =
                                    info.outputData.getString(EXTRA_UPLOAD_URL_RESULT)
                                ImageUploaderWorkHelper.Status.Success(profileImageUrl ?: "")
                            }

                            ImageUploaderWorkHelper.Result.FAILURE -> {
                                val errorResult = info.outputData.getInt(
                                    EXTRA_UPLOAD_ERROR_RESULT, 0
                                )
                                ImageUploaderWorkHelper.Status.Error(errorResult.takeIf { it != 0 })
                            }
                        }
                        workManager.pruneWork()
                    }
                    status?.let(emitter::onNext)
                }
            }
            emitter.setCancellable { statusesByTagLiveData.removeObserver(observer) }
            statusesByTagLiveData.observeForever(observer)
        }.share()

    override fun uploadImage(filePath: String) {
        workManager.enqueue(
            workRequestBuilder.buildUpdateProfileAvatarWorkerRequest(filePath)
        )
    }
}

enum class StateWorkLoading {
    SUCCESS,
    ERROR,
    NON,
    LOST_CONNECT
}
