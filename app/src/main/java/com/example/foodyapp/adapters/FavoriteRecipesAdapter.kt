package cash.now.cshnw.adapters


import android.annotation.SuppressLint
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cash.now.cshnw.R
import cash.now.cshnw.database.entitiy.FavoriteEntity
import cash.now.cshnw.databinding.FavoritRecipesRowLayoutBinding
import cash.now.cshnw.ui.fragments.favorit.FavoriteRecipesFragmentDirections
import cash.now.cshnw.viewmodels.MainViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar


class FavoriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {
    private var multiSelection = false
    lateinit var mActionMode: ActionMode
    private var selectedRecipes = arrayListOf<FavoriteEntity>()

    private var myViewHolder = arrayListOf<MyViewHolder>()
    private var favoriteRecipes = emptyList<FavoriteEntity>()
    lateinit var view: View

    class MyViewHolder(private val binding: FavoritRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoriteEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)
        myViewHolder.add(holder)
        view = holder.itemView.rootView

        /**Single Click Listener*/
        holder.itemView.findViewById<ConstraintLayout>(R.id.favorit_recipesRowLayout)
            .setOnClickListener {
                if (multiSelection) {
                    applySelection(holder, currentRecipe)
                } else {
                    val action =
                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                            currentRecipe.result
                        )
                    holder.itemView.findNavController().navigate(action)
                }
            }

        /**Long Click Listener*/
        holder.itemView.findViewById<ConstraintLayout>(R.id.favorit_recipesRowLayout)
            .setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this)
                    applySelection(holder, currentRecipe)
                    true
                } else {
                    multiSelection = false
                    false
                }

            }

    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.stroke_color)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.purple_500)
            applyActionModeTitle()

        }
    }

    @SuppressLint("CutPasteId")
    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.findViewById<ConstraintLayout>(R.id.favorit_recipesRowLayout)
            .setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))

        val cardView = holder.itemView.findViewById<MaterialCardView>(R.id.favorite_row_cardView)
        cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
        cardView.cardElevation = 0F
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> mActionMode.finish()
            1 -> mActionMode.title = "${selectedRecipes.size} item Selected"
            else -> mActionMode.title = "${selectedRecipes.size} items Selected"
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }


    /**THIS IS CONCEPTUAL action mode*/
    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatueBarColor(R.color.contextualStatueBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {

        if (menu?.itemId == R.id.delete_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipes(it)
            }
            snackBar("${selectedRecipes.size} Recipes Deleted")
        }
        multiSelection = false
        selectedRecipes.clear()
        mActionMode.finish()
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolder.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.stroke_color)
        }
        applyStatueBarColor(R.color.statusActionBarColor)
        multiSelection = false
        selectedRecipes.clear()


    }

    /**requireActivity.window.statueBarColor*/
    private fun applyStatueBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)

    }

    fun setData(newFavoriteRecipes: List<FavoriteEntity>) {
        val favoriteRecipesDiffUtil =
            MyDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }


    fun snackBar(message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Okay") {
        }.show()
    }


    fun clearContext() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}