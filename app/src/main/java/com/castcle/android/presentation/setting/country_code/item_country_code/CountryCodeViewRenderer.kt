package com.castcle.android.presentation.setting.country_code.item_country_code

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemCountryCodeBinding
import com.castcle.android.presentation.setting.country_code.CountryCodeListener
import io.reactivex.disposables.CompositeDisposable

class CountryCodeViewRenderer : CastcleViewRenderer<CountryCodeViewEntity,
    CountryCodeViewHolder,
    CountryCodeListener>(R.layout.item_country_code) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CountryCodeListener,
        compositeDisposable: CompositeDisposable
    ) = CountryCodeViewHolder(
        ItemCountryCodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}