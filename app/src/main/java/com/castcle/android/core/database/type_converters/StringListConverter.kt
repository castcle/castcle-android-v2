package com.castcle.android.core.database.type_converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val type = object : TypeToken<List<String>?>() {}.type

    @TypeConverter
    fun fromEntity(item: List<String>?): String? = item?.let { Gson().toJson(it, type) }

    @TypeConverter
    fun toEntity(item: String?): List<String>? = item?.let { Gson().fromJson(item, type) }

}