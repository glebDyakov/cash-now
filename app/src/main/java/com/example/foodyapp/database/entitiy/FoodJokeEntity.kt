package com.example.foodyapp.database.entitiy

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodyapp.models.FoodJoke
import com.example.foodyapp.utils.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {

    @PrimaryKey(autoGenerate = false)
    var id = 0
}