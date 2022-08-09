package com.castcle.android.domain.content

import com.castcle.android.data.content.entity.CreateContentRequest
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.entity.ParticipateEntity

interface ContentRepository {
    suspend fun createContent(body: CreateContentRequest, userId: String)
    suspend fun getContent(contentId: String, sessionId: Long): ContentEntity
    suspend fun getContentParticipate(contentId: String): List<Pair<String, ParticipateEntity>>
}