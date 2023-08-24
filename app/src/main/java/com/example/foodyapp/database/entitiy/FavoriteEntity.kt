package cash.now.cshnw.database.entitiy

import android.graphics.Color
import android.security.identity.AccessControlProfileId
import androidx.room.Entity
import androidx.room.PrimaryKey
import cash.now.cshnw.R
import cash.now.cshnw.models.Result
import cash.now.cshnw.utils.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result,
    var ischecked: Boolean

) {
}