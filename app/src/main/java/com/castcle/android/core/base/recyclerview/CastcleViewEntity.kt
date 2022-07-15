package com.castcle.android.core.base.recyclerview

interface CastcleViewEntity {

    val uniqueId: String

    fun sameAs(isSameItem: Boolean, target: Any?): Boolean

    fun viewType(): Int

}