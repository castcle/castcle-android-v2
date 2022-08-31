package com.castcle.android.presentation.setting.country_code.item_country_code

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.metadata.entity.CountryCodeEntity

data class CountryCodeViewEntity(
    val countryCode: CountryCodeEntity = CountryCodeEntity(),
    override val uniqueId: String = "${R.layout.item_country_code}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<CountryCodeViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<CountryCodeViewEntity>() == this
    }

    override fun viewType() = R.layout.item_country_code

}