package com.example.shoppingdemo.database

import androidx.room.TypeConverter
import com.example.shoppingdemo.data.Categories
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun toCategoriesList(missionPaymentList: String?): List<Categories?>? {
        val typeToken: Type = object : TypeToken<List<Categories?>?>() {}.type
        return Gson().fromJson<List<Categories?>>(missionPaymentList, typeToken)
    }

    @TypeConverter
    fun fromCategoriesList(missionPaymentsList: List<Categories?>?): String? {
        return Gson().toJson(missionPaymentsList)
    }
}