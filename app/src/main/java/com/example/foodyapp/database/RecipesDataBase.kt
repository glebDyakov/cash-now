package cash.now.cshnw.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cash.now.cshnw.database.entitiy.FavoriteEntity
import cash.now.cshnw.database.entitiy.FoodJokeEntity
import cash.now.cshnw.database.entitiy.RecipesEntity

@Database(entities = [RecipesEntity::class,FavoriteEntity::class,FoodJokeEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipesDataBase() : RoomDatabase() {

    abstract fun RecipesDao() : RecipesDao

    }

