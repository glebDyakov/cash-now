package cash.now.cshnw.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cash.now.cshnw.R
import cash.now.cshnw.models.ExtendedIngredient
import cash.now.cshnw.utils.Constants.Companion.BASE_IMAGE_URL
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    var ingredientlist = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingerediants_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredientlist[position]

        holder.itemView.findViewById<ImageView>(R.id.ingrediant_imageView)
            .load(BASE_IMAGE_URL + currentIngredient.image){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }

        holder.itemView.findViewById<TextView>(R.id.ingrediant_name).text =
            currentIngredient.name.capitalize(
                Locale.ROOT
            )

        holder.itemView.findViewById<TextView>(R.id.ingreiant_amount).text =
            currentIngredient.amount.toString()

        holder.itemView.findViewById<TextView>(R.id.ingreiant_unit).text =
            currentIngredient.unit

        holder.itemView.findViewById<TextView>(R.id.ingrediant_consistancy).text =
            currentIngredient.consistency

        holder.itemView.findViewById<TextView>(R.id.ingrediant_original).text =
            currentIngredient.original

    }

    override fun getItemCount(): Int {
        return ingredientlist.size
    }

    fun setData(extendedIngredient: List<ExtendedIngredient>){
        val ingredientsDiffUtil = MyDiffUtil(ingredientlist, extendedIngredient)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientlist = extendedIngredient
        diffUtilResult.dispatchUpdatesTo(this)
    }
}