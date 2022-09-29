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
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.content.entity.CreateContentRequest
import com.castcle.android.data.user.entity.CreateQuoteCastRequest
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.HashtagEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
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
    private val database: CastcleDatabase,
    private val mapper: NewCastMapper,
    private val searchRepository: SearchRepository,
    private val userId: String?,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val createContentError = MutableLiveData<Throwable>()

    val createContentSuccess = MutableLiveData<Unit>()

    val currentUser = MutableStateFlow<UserEntity?>(null)

    val imageUri = MutableStateFlow<List<Uri>>(listOf())

    val quoteCast = MutableStateFlow<List<CastcleViewEntity>?>(null)

    val hashtags = MutableLiveData<List<HashtagEntity>>()

    val mentions = MutableLiveData<List<UserEntity>>()

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
            currentUser.value = if (userId != null) {
                database.user().get(userId).firstOrNull()
            } else {
                database.user().get(UserType.People).firstOrNull()
            }
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
            contentRepository.createContent(body = body, userId = currentUser.value?.id.orEmpty())
            createContentSuccess.postValue(Unit)
        }
    }

    fun createQuoteCast(contentId: String, message: String) {
        launch(onError = createContentError::postValue) {
            val body = CreateQuoteCastRequest(
                contentId = contentId,
                message = message,
            )
            userRepository.createQuoteCast(body = body, userId = currentUser.value?.id.orEmpty())
            createContentSuccess.postValue(Unit)
        }
    }

    fun getHashtag(keyword: String) {
        launch {
            hashtags.postValue(searchRepository.getHashtags(keyword))
        }
    }

    fun getMentions(keyword: String) {
        launch {
            mentions.postValue(searchRepository.getMentions(keyword))
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