package com.castcle.android.domain.metadata.entity

import android.os.Parcelable
import androidx.room.*
import com.castcle.android.core.constants.TABLE_COUNTRY_CODE
import com.castcle.android.data.metadata.entity.CountryCodeResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_COUNTRY_CODE)
@Parcelize
data class CountryCodeEntity(
    @ColumnInfo(name = "${TABLE_COUNTRY_CODE}_code", defaultValue = "")
    val code: String = "TH",
    @ColumnInfo(name = "${TABLE_COUNTRY_CODE}_dialCode", defaultValue = "")
    @PrimaryKey
    val dialCode: String = "+66",
    @ColumnInfo(name = "${TABLE_COUNTRY_CODE}_name", defaultValue = "")
    val name: String = "Thailand",
) : Parcelable {

    companion object {
        fun map(response: List<CountryCodeResponse>?) = response.orEmpty().map {
            CountryCodeEntity(
                code = it.code.orEmpty(),
                dialCode = it.dialCode.orEmpty(),
                name = it.name.orEmpty(),
            )
        }
    }

}