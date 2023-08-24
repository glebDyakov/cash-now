package cash.now.cshnw.database.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import cash.now.cshnw.models.FoodRecipe
import cash.now.cshnw.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}