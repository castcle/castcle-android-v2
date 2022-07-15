package com.castcle.android.domain.feed

import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import io.reactivex.Completable
import io.reactivex.Single

interface FeedRepository {
    fun getAccessToken(): Single<AccessTokenEntity>
    fun fetchGuestAccessToken(): Completable
}