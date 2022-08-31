package com.castcle.android.core.glide

import com.bumptech.glide.load.model.GlideUrl

class GlideUrlWithQueryParameter(private val url: String?) : GlideUrl(url.safeUrl()) {

    override fun toString(): String = super.getCacheKey()

    override fun getCacheKey(): String = url?.split("?")?.firstOrNull() ?: url.orEmpty()

    companion object {
        private fun String?.safeUrl(): String {
            return if (isNullOrBlank()) {
                "https://castcle-public.s3.amazonaws.com/assets/no-image-placeholder.png"
            } else {
                this
            }
        }
    }

}