package com.castcle.android.data.search.mapper

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.search.entity.SearchEntity
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class SearchResponseMapper {

    data class SearchResponseResult(
        val cast: List<CastEntity>,
        val search: List<SearchEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        searchResponse: BaseResponse<out List<Any>>?,
        ownerUserId: List<String>,
        sessionId: Long
    ): SearchResponseResult {
        val userItems = searchResponse?.includes
            ?.users
            ?.map { UserEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val castItems = searchResponse?.includes
            ?.casts
            ?.map { CastEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val searchItems = searchResponse?.payload.orEmpty().mapNotNull { response ->
            when (response) {
                is UserResponse -> {
                    val user = UserEntity.map(ownerUserId, response).also(userItems::add)
                    SearchEntity(
                        originalUserId = user.id,
                        sessionId = sessionId,
                    )
                }
                is CastResponse -> {
                    val cast = CastEntity.map(ownerUserId, response).also(castItems::add)
                    val referencedCast = castItems.find { it.id == response.referencedCasts?.id }
                    SearchEntity(
                        originalCastId = cast.id,
                        originalUserId = cast.authorId,
                        referenceCastId = referencedCast?.id,
                        referenceUserId = referencedCast?.authorId,
                        sessionId = sessionId,
                    )
                }
                else -> null
            }
        }
        return SearchResponseResult(
            cast = castItems.distinctBy { it.id },
            search = searchItems,
            user = userItems.distinctBy { it.id },
        )
    }

}