package cash.now.cshnw.database.entitiy

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import cash.now.cshnw.models.FoodJoke
import cash.now.cshnw.utils.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {

    @PrimaryKey(autoGenerate = false)
    var id = 0
}