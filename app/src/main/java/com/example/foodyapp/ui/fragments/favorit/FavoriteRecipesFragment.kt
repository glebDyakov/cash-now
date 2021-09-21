package com.example.foodyapp.ui.fragments.favorit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.R
import com.example.foodyapp.adapters.FavoriteRecipesAdapter
import com.example.foodyapp.databinding.FragmentFavoriteRecipesBinding
import com.example.foodyapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val favoriteRecipesAdapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter(requireActivity(),mainViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = favoriteRecipesAdapter
        binding.favoriteRecyclerView.adapter = favoriteRecipesAdapter

        /**Don't forget to assign setHasOptionMenu to : true
         * as it's the very important to show the icon on the bar*/
        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.deleteAll_favorite_recipe_menu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("All recipes Deleted")
        }

        return super.onOptionsItemSelected(item)
    }

    fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).setAction("Okay") {
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        favoriteRecipesAdapter.clearContext()
    }


}