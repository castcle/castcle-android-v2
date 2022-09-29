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

package com.castcle.android.domain.setting.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.castcle.android.core.constants.TABLE_CONFIG
import com.castcle.android.data.setting.entity.UpdateVersionResponse
import com.castcle.android.domain.wallet.type.WalletType
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.gson.Gson

@Entity(tableName = TABLE_CONFIG)
data class ConfigEntity(
    @ColumnInfo(name = "${TABLE_CONFIG}_adsEnable", defaultValue = "false")
    val adsEnable: Boolean = false,
    @ColumnInfo(name = "${TABLE_CONFIG}_airdropBannerEnable", defaultValue = "false")
    val airdropBannerEnable: Boolean = false,
    @ColumnInfo(name = "${TABLE_CONFIG}_farmingEnable", defaultValue = "false")
    val farmingEnable: Boolean = false,
    @ColumnInfo(name = "${TABLE_CONFIG}_forceUpdateVersion", defaultValue = "")
    val forceUpdateVersion: UpdateVersionEntity = UpdateVersionEntity(),
    @ColumnInfo(name = "${TABLE_CONFIG}_id", defaultValue = TABLE_CONFIG)
    @PrimaryKey
    val id: String = TABLE_CONFIG,
    @ColumnInfo(name = "${TABLE_CONFIG}_playStoreUpdateVersion", defaultValue = "")
    val playStoreUpdateVersion: UpdateVersionEntity = UpdateVersionEntity(),
    @ColumnInfo(name = "${TABLE_CONFIG}_walletType", defaultValue = "1")
    val walletType: WalletType = WalletType.WebVew,
) {

    companion object {
        fun map(result: MutableMap<String, FirebaseRemoteConfigValue>) = ConfigEntity(
            adsEnable = result["ads_enable"]?.asBoolean() ?: false,
            airdropBannerEnable = result["banner_early_airdrop_enable"]?.asBoolean() ?: false,
            farmingEnable = result["farming_enable"]?.asBoolean() ?: false,
            forceUpdateVersion = result["force_version"]?.asString()
                ?.let { Gson().fromJson(it, UpdateVersionResponse::class.java) }
                ?.let { UpdateVersionEntity.map(it) }
                ?: UpdateVersionEntity(),
            playStoreUpdateVersion = result["current_version"]?.asString()
                ?.let { Gson().fromJson(it, UpdateVersionResponse::class.java) }
                ?.let { UpdateVersionEntity.map(it) }
                ?: UpdateVersionEntity(),
            walletType = WalletType.getFromId(result["menu_wallet"]?.asString()?.toIntOrNull()),
        )
    }

}