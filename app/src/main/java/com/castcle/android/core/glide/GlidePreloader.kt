package com.castcle.android.core.glide

import android.content.Context
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class GlidePreloader(private val context: Context) {

    fun loadCast(item: CastEntity?) {
        startPreload(item?.linkPreview)
        item?.image?.forEach {
            startPreload(it.thumbnail)
        }
    }

    fun loadCast(items: List<CastEntity>?) {
        items?.forEach { loadCast(it) }
    }

    fun loadUser(item: UserEntity?) {
        startPreload(item?.avatar?.thumbnail)
        startPreload(item?.cover?.thumbnail)
    }

    fun loadUser(items: List<UserEntity>?) {
        items?.forEach { loadUser(it) }
    }

    private fun startPreload(url: String?) {
        if (!url.isNullOrBlank()) {
            GlideApp.with(context)
                .load(url)
                .preload()
        }
    }

}