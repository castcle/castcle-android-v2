package com.castcle.android.presentation.setting.register_mobile

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.presentation.setting.register_mobile.item_register_mobile.RegisterMobileViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RegisterMobileViewModel(
    private val database: CastcleDatabase
) : BaseViewModel() {

    val views = listOf(RegisterMobileViewEntity())

}