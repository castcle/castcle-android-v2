package com.castcle.android.presentation.new_cast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.content.entity.CreateContentRequest
import com.castcle.android.data.user.entity.CreateQuoteCastRequest
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image_item.FeedImageItemViewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import java.io.File

@KoinViewModel
class NewCastViewModel(
    private val contentRepository: ContentRepository,
    val database: CastcleDatabase,
    val mapper: NewCastMapper,
    val userId: String,
    val userRepository: UserRepository,
) : BaseViewModel() {

    val createContentError = MutableLiveData<Throwable>()

    val createContentSuccess = MutableLiveData<Unit>()

    val currentUser = MutableStateFlow<UserEntity?>(null)

    val imageUri = MutableStateFlow<List<Uri>>(listOf())

    val quoteCast = MutableStateFlow<List<CastcleViewEntity>?>(null)

    val imageItems = imageUri.map { uri ->
        uri.mapIndexed { index, each ->
            FeedImageItemViewEntity(
                itemCount = uri.size,
                uniqueId = index.toString(),
                uri = each,
            )
        }
    }

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        launch {
            currentUser.value = database.user().get(userId).firstOrNull()
        }
    }

    fun getQuoteCast(quoteCastId: String?) {
        launch {
            quoteCastId?.let { database.cast().get(quoteCastId) }
                ?.let { mapper.apply(it) }
                ?.let { quoteCast.value = listOf(it) }
        }
    }

    fun createCast(context: Context, message: String) {
        launch(onError = createContentError::postValue) {
            val body = CreateContentRequest(
                castcleId = currentUser.value?.castcleId,
                payload = CreateContentRequest.Payload(
                    message = message,
                    photo = CreateContentRequest.Photo(
                        contents = imageUri.value.map {
                            CreateContentRequest.Contents(image = imageUriToBase64(context, it))
                        }
                    ),
                ),
            )
            contentRepository.createContent(body = body, userId = userId)
            createContentSuccess.postValue(Unit)
        }
    }

    fun createQuoteCast(contentId: String, message: String) {
        launch(onError = createContentError::postValue) {
            val body = CreateQuoteCastRequest(
                contentId = contentId,
                message = message,
            )
            userRepository.createQuoteCast(body = body, userId = userId)
            createContentSuccess.postValue(Unit)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext", "DEPRECATION")
    private suspend fun imageUriToBase64(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile("image", ".jpeg", context.cacheDir)
            val bitmap = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, tempFile.outputStream())
            Base64.encodeToString(tempFile.readBytes(), Base64.NO_WRAP)
        }
    }

}