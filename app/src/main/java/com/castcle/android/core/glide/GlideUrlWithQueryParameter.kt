package com.castcle.android.core.glide

import com.bumptech.glide.load.model.GlideUrl

class GlideUrlWithQueryParameter(private val url: String?) : GlideUrl(url) {

    override fun toString(): String = super.getCacheKey()

    override fun getCacheKey(): String = url?.split("?")?.firstOrNull() ?: url ?: ""

}