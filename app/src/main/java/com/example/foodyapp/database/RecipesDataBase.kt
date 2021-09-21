package com.example.foodyapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodyapp.database.entitiy.FavoriteEntity
import com.example.foodyapp.database.entitiy.FoodJokeEntity
import com.example.foodyapp.database.entitiy.RecipesEntity

@Database(entities = [RecipesEntity::class,FavoriteEntity::class,FoodJokeEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipesDataBase() : RoomDatabase() {

    abstract fun RecipesDao() : RecipesDao

    }

