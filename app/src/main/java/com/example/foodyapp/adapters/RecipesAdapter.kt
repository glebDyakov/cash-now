package cash.now.cshnw.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cash.now.cshnw.databinding.ItemViewBinding
import cash.now.cshnw.models.FoodRecipe
import cash.now.cshnw.models.Result

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    class MyViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view = ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipes = recipes[position]
        holder.bind(currentRecipes)

    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe) {
        val recipeDiffUtil = MyDiffUtil(recipes, newData.results)
        val diffResult = DiffUtil.calculateDiff(recipeDiffUtil)
        recipes = newData.results
        diffResult.dispatchUpdatesTo(this)

    }
}