package com.castcle.android.core.custom_view.participate_bar

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.cast.entity.CastEntity

interface ParticipateBarListener : CastcleListener {
    fun onCommentClicked(cast: CastEntity)
    fun onContentFarmingClicked(cast: CastEntity)
    fun onLikeClicked(cast: CastEntity)
    fun onRecastClicked(cast: CastEntity)
}