package cash.now.cshnw.database

import androidx.room.*
import cash.now.cshnw.database.entitiy.FavoriteEntity
import cash.now.cshnw.database.entitiy.FoodJokeEntity
import cash.now.cshnw.database.entitiy.RecipesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("Select* FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("Select* FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke():Flow<List<FoodJokeEntity>>

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

    @Delete
    suspend fun deleteAllFavoriteRecipes(favoriteEntity: FavoriteEntity)
}