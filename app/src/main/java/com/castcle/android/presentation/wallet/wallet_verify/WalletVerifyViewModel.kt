/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.wallet.wallet_verify

import com.castcle.android.R
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.account.item_menu.AccountMenuViewEntity
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewEntity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletVerifyViewModel(
    database: CastcleDatabase,
) : BaseViewModel() {

    val views = database.user().retrieveWithLinkSocial(UserType.People)
        .filterNotNull()
        .map { result ->
            listOf(
                AccountTitleViewEntity(titleId = R.string.fragment_wallet_verify_title_1),
                AccountMenuViewEntity(
                    action = {
                        when {
                            result.user.email.isNullOrBlank() -> it.onRegisterEmailClicked()
                            result.user.verifiedEmail == false -> it.onResentVerifyEmailClicked()
                        }
                    },
                    description = if (result.user.email.isNullOrBlank() || result.user.verifiedEmail == true) {
                        null
                    } else {
                        R.string.not_verify
                    },
                    detail = result.user.email?.ifBlank { null } ?: R.string.unregistered,
                    showArrow = result.user.verifiedEmail == false,
                    titleId = R.string.email,
                ),
                AccountMenuViewEntity(
                    action = { it.onRequestOtpClicked(OtpType.Mobile) },
                    detail = if (result.user.mobileCountryCode.isNullOrBlank() || result.user.mobileNumber.isNullOrBlank()) {
                        R.string.unregistered
                    } else {
                        "(${result.user.mobileCountryCode}) ${result.user.mobileNumber}"
                    },
                    titleId = R.string.mobile_number,
                ),
            )
        }

}