package com.castcle.android.domain.content.entity

import com.castcle.android.core.base.response.ParticipateResponse

data class ParticipateEntity(
    val commented: Boolean = false,
    val farming: Boolean = false,
    val liked: Boolean = false,
    val quoted: Boolean = false,
    val recasted: Boolean = false,
    val reported: Boolean = false,
) {

    companion object {
        fun map(response: ParticipateResponse?) = ParticipateEntity(
            commented = response?.commented ?: false,
            farming = response?.farming ?: false,
            liked = response?.liked ?: false,
            quoted = response?.quoted ?: false,
            recasted = response?.recasted ?: false,
            reported = response?.reported ?: false,
        )
    }

}