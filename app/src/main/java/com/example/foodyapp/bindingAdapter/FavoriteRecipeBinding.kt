package cash.now.cshnw.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import cash.now.cshnw.adapters.FavoriteRecipesAdapter
import cash.now.cshnw.database.entitiy.FavoriteEntity
import org.jsoup.Jsoup

class FavoriteRecipeBinding {
    companion object {
        @BindingAdapter("setDataForFavoriteEntity", "setDataForAdapter", requireAll = true)
        @JvmStatic
        fun View.setDataAndVisibility(
            favoriteEntity: List<FavoriteEntity>?,
            mAdapter: FavoriteRecipesAdapter?
        ) {
            if (favoriteEntity.isNullOrEmpty()) {
                when (this) {
                    is ImageView -> {
                        this.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        this.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        this.visibility = View.INVISIBLE
                    }

                }
            } else {
                when (this) {
                    is ImageView -> {
                        this.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        this.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        this.visibility = View.VISIBLE
                        mAdapter?.setData(favoriteEntity)
                    }
                }
            }
        }


    }
}