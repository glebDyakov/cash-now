package com.example.foodyapp.database.entitiy

import android.graphics.Color
import android.security.identity.AccessControlProfileId
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodyapp.R
import com.example.foodyapp.models.Result
import com.example.foodyapp.utils.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result,
    var ischecked: Boolean

) {
}