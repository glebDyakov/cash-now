package com.example.foodyapp.database

import androidx.room.TypeConverter
import com.example.foodyapp.database.entitiy.FavoriteEntity
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    var gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipes(data: String): FoodRecipe {
        var listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        var listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }
}