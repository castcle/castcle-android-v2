package com.castcle.android.data.content

import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class ContentRepositoryImpl(
    private val api: ContentApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
) : ContentRepository {

    override suspend fun getContent(contentId: String, sessionId: Long): ContentEntity {
        val ownerUserId = database.user().get().map { it.id }
        val response = apiCall { api.getContent(contentId = contentId) }
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

}