package com.castcle.android.domain.content

import com.castcle.android.domain.content.entity.ContentEntity

interface ContentRepository {
    suspend fun getContent(contentId: String, sessionId: Long): ContentEntity
}