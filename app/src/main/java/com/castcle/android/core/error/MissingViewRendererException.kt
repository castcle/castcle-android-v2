package com.castcle.android.core.error

class MissingViewRendererException(
    val viewId: Int,
    val viewResourceName: String,
) : Throwable("View renderer id $viewId ($viewResourceName) not found in adapter.")