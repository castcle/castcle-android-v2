package com.castcle.android.presentation.following_followers

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.user.entity.FollowingFollowersWithResultEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.search_result.item_search_people.SearchPeopleViewEntity
import org.koin.core.annotation.Factory

@Factory
class FollowingFollowersMapper {

    fun apply(item: FollowingFollowersWithResultEntity): CastcleViewEntity {
        return SearchPeopleViewEntity(
            uniqueId = item.user?.id ?: "",
            user = item.user ?: UserEntity(),
        )
    }

}