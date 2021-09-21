package com.example.foodyapp.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.example.foodyapp.database.entitiy.FavoriteEntity
import com.example.foodyapp.database.entitiy.RecipesEntity
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.utils.NetworkResult

class RecipesBinding {
    companion object {
        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun <T> TextView.errorTextViewVisibility(
            apiResponse: NetworkResult<T>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                this.text = apiResponse.message.toString()
                this.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                this.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                this.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun ImageView.errorImageViewVisibility(
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                this.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                this.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                this.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("noInternetConnection")
        @JvmStatic
        fun CardView.noInternetConnection(
            value: Boolean
        ) {
            if (value) {
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }


        }



    }
}