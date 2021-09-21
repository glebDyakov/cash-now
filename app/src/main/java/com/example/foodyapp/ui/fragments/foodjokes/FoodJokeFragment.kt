package com.example.foodyapp.ui.fragments.foodjokes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodyapp.R
import com.example.foodyapp.databinding.FragmentFoodJokeBinding
import com.example.foodyapp.utils.Constants.Companion.API_KEY
import com.example.foodyapp.utils.NetworkResult
import com.example.foodyapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {
    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    private var foodJoke = "NO FOOD JOKE"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)
        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    binding.foodeJokeTextView.text = response.data?.text
                    foodJoke = response.data?.text.toString()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    offlineCache()
                }

                is NetworkResult.Loading -> {
                    Log.v("FoodJokeFragment", "Loading")
                }
            }

        })


        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu){
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, foodJoke)
                type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }


    fun offlineCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner, { foodEntitiy ->
                if (!foodEntitiy.isNullOrEmpty()) {
                    binding.foodeJokeTextView.text = foodEntitiy[0].foodJoke.text
                    foodJoke = foodEntitiy[0].foodJoke.text
                }
            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}