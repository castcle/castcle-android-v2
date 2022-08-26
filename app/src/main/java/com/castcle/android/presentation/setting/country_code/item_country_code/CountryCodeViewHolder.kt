package com.castcle.android.presentation.setting.country_code.item_country_code

import android.annotation.SuppressLint
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemCountryCodeBinding
import com.castcle.android.presentation.setting.country_code.CountryCodeListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class CountryCodeViewHolder(
    private val binding: ItemCountryCodeBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: CountryCodeListener
) : CastcleViewHolder<CountryCodeViewEntity>(binding.root) {

    override var item = CountryCodeViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onCountryCodeClicked(item.countryCode)
        }
    }

    override fun bind(bindItem: CountryCodeViewEntity) {
        binding.root.text = "${item.countryCode.dialCode}  ${item.countryCode.name}"
    }

}