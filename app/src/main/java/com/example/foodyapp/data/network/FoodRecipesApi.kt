package com.example.foodyapp.data.network

import com.example.foodyapp.models.FoodJoke
import com.example.foodyapp.models.FoodRecipe
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {


    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries : Map<String,String>
    ):Response<FoodRecipe>


    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQueries : Map<String,String>
    ):Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") queries : String
    ):Response<FoodJoke>


}