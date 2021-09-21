package com.example.foodyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.foodyapp.R
import com.example.foodyapp.adapters.PagerAdapter
import com.example.foodyapp.database.entitiy.FavoriteEntity
import com.example.foodyapp.ui.fragments.ingeredients.IngredientsFragment
import com.example.foodyapp.ui.fragments.instruction.InstructionsFragment
import com.example.foodyapp.ui.fragments.overview.OverviewFragment
import com.example.foodyapp.utils.Constants.Companion.RECIPES_RESULT
import com.example.foodyapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0
    private lateinit var menuItem : MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()

        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPES_RESULT, args.result)

        val pageradapter = PagerAdapter(resultBundle, fragments, this)

        val viewpager = findViewById<ViewPager2>(R.id.viewPager2)
        viewpager.isUserInputEnabled = false
        viewpager.apply {
            adapter = pageradapter
        }
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout,viewpager){tab,position ->
            tab.text = title[position]
        }.attach()

    }

    /**this override when you need to declare an option in you menu folder so it appears*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorite_menu)
        checkSavedItem(menuItem)
        return true
    }

    private fun checkSavedItem(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoritesEntity ->
            try {
                favoritesEntity.forEach {
                    if (it.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = it.id
                        recipeSaved = true

                    }
                }


            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }


    /**This override to handel (on click) when any thing in the tool bar is pressed*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorite_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorite_menu && recipeSaved) {
            deleteFavoriteRecipe(item)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity =
            FavoriteEntity(
                0,
                args.result,
                true
            )
        mainViewModel.insertFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true
    }

    private fun deleteFavoriteRecipe(item: MenuItem) {
        val favoritesEntity =
            FavoriteEntity(
                savedRecipeId,
                args.result,
                false
            )
        mainViewModel.deleteFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
    }


    private fun showSnackBar(message: String) {
        val detailsLayout = findViewById<ConstraintLayout>(R.id.detailsLayout)
        Snackbar.make(detailsLayout, message, Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}