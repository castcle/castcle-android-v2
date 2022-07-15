package com.castcle.android.domain.cast.type

import androidx.annotation.DrawableRes
import com.castcle.android.R

sealed class WebType(val id: String, @DrawableRes val icon: Int) {

    object Facebook : WebType(icon = R.drawable.ic_web_facebook, id = "facebook")
    object Medium : WebType(icon = R.drawable.ic_web_meduim, id = "medium")
    object Reddit : WebType(icon = R.drawable.ic_web_reddit, id = "reddit")
    object RssFeed : WebType(icon = R.drawable.ic_web_rss_feed, id = "rssfeed")
    object Twitter : WebType(icon = R.drawable.ic_web_twitter, id = "twitter")
    object Web : WebType(icon = R.drawable.ic_web, id = "web")
    object Youtube : WebType(icon = R.drawable.ic_web_youtube, id = "youtube")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Facebook.id -> Facebook
            Medium.id -> Medium
            Reddit.id -> Reddit
            RssFeed.id -> RssFeed
            Twitter.id -> Twitter
            Youtube.id -> Youtube
            else -> Web
        }
    }

}