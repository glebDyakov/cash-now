package cash.now.cshnw.data

import cash.now.cshnw.data.network.FoodRecipesApi
import cash.now.cshnw.models.FoodJoke
import cash.now.cshnw.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipesApi: FoodRecipesApi) {

    suspend fun getRecipes (queries : Map<String,String>) : Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes (searchQueries : Map<String,String>) : Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQueries)
    }


    suspend fun getFoodJoke (searchQueries : String) : Response<FoodJoke> {
        return foodRecipesApi.getFoodJoke(searchQueries)
    }



}