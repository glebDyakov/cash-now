package com.example.foodyapp.bindingAdapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foodyapp.R
import com.example.foodyapp.models.Result
import com.example.foodyapp.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

@BindingAdapter("onClickListener")
fun ConstraintLayout.onClickListener(result: Result) {
    this.setOnClickListener {
        try {
            val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity2(result)
            this.findNavController().navigate(action)
        } catch (e: Exception) {
            Log.d("onClick", e.message.toString())
        }
    }
}


@BindingAdapter("loadImageUrl")
fun ImageView.loadImageUrl(imageUrl: String) {
    this.load(imageUrl) {
        crossfade(600)
        error(R.drawable.ic_error_placeholder)
    }
}

@BindingAdapter("setNumberoFlikes")
fun TextView.setNumberoFlikes(likes: Int) {
    this.text = likes.toString()
}

@BindingAdapter("setNumberoFminus")
fun TextView.setNumberoFminus(minus: Int) {
    this.text = minus.toString()
}

@BindingAdapter("applyColor")
fun applyColor(view: View, vegan: Boolean) {
    if (vegan) {
        when (view) {
            is TextView -> {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
            }

            is ImageView -> {
                view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
            }
        }
    }
}

@BindingAdapter("parseHtml")
fun TextView.parseHtml(description : String?){
    if (description != null){
        val desc = Jsoup.parse(description).text()
        this.text = desc
    }
}

class RecipesRowBinding {
    companion object {
//
//        @BindingAdapter("loadImageUrl")
//        @JvmStatic
//        fun ImageView.loadImageUrl(imageUrl : String) {
//            this.load(imageUrl){
//                crossfade(600)
//            }
//        }
//
//        @BindingAdapter("setNumberoFlikes")
//        @JvmStatic
//        fun TextView.setNumberoFlikes(likes: Int) {
//            this.text = likes.toString()
//        }
//
//        @BindingAdapter("setNumberoFminus")
//        @JvmStatic
//        fun TextView.setNumberoFminus(minus: Int) {
//            this.text = minus.toString()
//        }
//
//        @BindingAdapter("applyColor")
//        @JvmStatic
//        fun applyColor(view: View, vegan: Boolean) {
//            if (vegan) {
//                when (view) {
//                    is TextView -> {
//                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
//                    }
//
//                    is ImageView -> {
//                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
//                    }
//                }
//            }
//        }
    }
}