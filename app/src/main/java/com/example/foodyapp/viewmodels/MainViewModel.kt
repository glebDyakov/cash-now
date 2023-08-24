package cash.now.cshnw.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import cash.now.cshnw.data.Repository
import cash.now.cshnw.database.entitiy.FavoriteEntity
import cash.now.cshnw.database.entitiy.FoodJokeEntity
import cash.now.cshnw.database.entitiy.RecipesEntity
import cash.now.cshnw.models.FoodJoke
import cash.now.cshnw.models.FoodRecipe
import cash.now.cshnw.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.Exception

//@HiltViewModel
class MainViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    /** DATABASE */

    val readRecipe: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoriteEntity>> = repository.local.readFavoriteDatabase().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodRecipes(favoriteEntity)
        }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    fun deleteFavoriteRecipes(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoriteEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineFoodJokeCache(foodJokeCache: FoodJoke) {
       val foodJokeEntity = FoodJokeEntity(foodJokeCache)
       insertFoodJoke(foodJokeEntity)
    }


    /** RETROFIT */
    var recipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()
    var searchRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            getRecipesSafeCall(queries)
        }
    }

    fun searchRecipes(searchQueries: Map<String, String>) {
        viewModelScope.launch {
            getSearchRecipesSafeCall(searchQueries)
        }
    }

    fun getFoodJoke (apiKey : String) =
        viewModelScope.launch {
            getFoodJokeSafeCall(apiKey)
        }


    private suspend fun getSearchRecipesSafeCall(searchQueries: Map<String, String>) {
        searchRecipeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQueries)
                searchRecipeResponse.value = handelFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            searchRecipeResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipeResponse.value = handelFoodRecipesResponse(response)

                //DATABASE
                val foodRecipe = recipeResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }

            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            recipeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private fun handelFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not Found")
            }
            response.isSuccessful -> {
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private suspend fun getFoodJokeSafeCall(searchQueries: String) {
        foodJokeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(searchQueries)
                foodJokeResponse.value = handelFoodJokeResponse(response)

                val foodJokeCache = foodJokeResponse.value!!.data
                if (foodJokeCache != null){
                    offlineFoodJokeCache(foodJokeCache)
                }
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Food Joke not found")
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }




    private fun handelFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        when {
            response.message().toString().contains("Timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}