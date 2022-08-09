package com.castcle.android.data.content

import androidx.room.withTransaction
import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.content.entity.CreateContentRequest
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.entity.ParticipateEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.user.entity.ProfileEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.ProfileType
import org.koin.core.annotation.Factory

@Factory
class ContentRepositoryImpl(
    private val api: ContentApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
) : ContentRepository {

    override suspend fun createContent(body: CreateContentRequest, userId: String) {
        val response = apiCall { api.createContent(body = body) }
        val ownerUserId = database.user().get().map { it.id }
        val content = CastEntity.map(ownerUserId, response?.payload)
        val sessionId = database.profile().getExistSessionIdByUserId(
            type = ProfileType.Profile,
            userId = userId,
        )
        val newProfile = sessionId.map {
            ProfileEntity(
                createdAt = content.createdAt.toMilliSecond(),
                originalCastId = content.id,
                originalUserId = content.authorId,
                sessionId = it,
                type = ProfileType.Content,
            )
        }
        database.withTransaction {
            database.cast().insert(content)
            database.profile().insert(newProfile)
            database.user().increaseCastCount(userId)
        }
    }

    override suspend fun deleteContent(contentId: String, userId: String) {
        apiCall { api.deleteContent(contentId = contentId) }
        database.withTransaction {
            database.cast().delete(contentId)
            database.profile().deleteByOriginalCast(contentId)
            database.profile().deleteByReferenceCast(contentId)
            database.search().deleteByOriginalCast(contentId)
            database.search().deleteByReferenceCast(contentId)
            database.user().decreaseCastCount(userId)
        }
    }

    override suspend fun getContent(contentId: String, sessionId: Long): ContentEntity {
        val response = apiCall { api.getContent(contentId = contentId) }
        val ownerUserId = database.user().get().map { it.id }
        val castItems = CastEntity.map(ownerUserId, response?.includes?.casts)
        val userItems = UserEntity.map(ownerUserId, response?.includes?.users)
        val cast = CastEntity.map(ownerUserId, response?.payload).also(castItems::add)
        val referencedCast = castItems.find { it.id == response?.payload?.referencedCasts?.id }
        glidePreloader.loadCast(items = castItems)
        glidePreloader.loadUser(items = userItems)
        database.cast().insert(castItems)
        database.user().upsert(userItems)
        return ContentEntity(
            originalCastId = cast.id,
            originalUserId = cast.authorId,
            referenceCastId = referencedCast?.id,
            referenceUserId = referencedCast?.authorId,
            sessionId = sessionId,
            type = ContentType.Content,
        )
    }

    override suspend fun getContentParticipate(contentId: String): List<Pair<String, ParticipateEntity>> {
        val response = apiCall { api.getContentParticipate(contentId = contentId) }
        return response.orEmpty().map {
            it.user?.id.orEmpty() to ParticipateEntity.map(it.participate)
        }
    }

}