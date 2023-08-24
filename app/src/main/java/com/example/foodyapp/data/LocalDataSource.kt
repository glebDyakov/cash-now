package cash.now.cshnw.data

import cash.now.cshnw.database.RecipesDao
import cash.now.cshnw.database.entitiy.FavoriteEntity
import cash.now.cshnw.database.entitiy.FoodJokeEntity
import cash.now.cshnw.database.entitiy.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    suspend fun insertRecipes (recipesEntity: RecipesEntity) {
       recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFoodRecipes (favoriteEntity: FavoriteEntity) {
        recipesDao.insertFavoriteRecipes(favoriteEntity)
    }

    suspend fun insertFoodJoke (foodJokeEntity: FoodJokeEntity) {
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    fun readFoodJoke() : Flow<List<FoodJokeEntity>>{
        return recipesDao.readFoodJoke()
    }

    fun readDatabase () : Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavoriteDatabase () : Flow<List<FavoriteEntity>>{
        return recipesDao.readFavoriteRecipes()
    }

    suspend fun deleteFavoriteRecipe (favoriteEntity: FavoriteEntity) {
        recipesDao.deleteAllFavoriteRecipes(favoriteEntity)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }

}