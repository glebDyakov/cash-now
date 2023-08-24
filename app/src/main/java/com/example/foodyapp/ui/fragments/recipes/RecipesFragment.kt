package cash.now.cshnw.ui.fragments.recipes

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cash.now.cshnw.R
import cash.now.cshnw.viewmodels.MainViewModel
import cash.now.cshnw.adapters.RecipesAdapter
import cash.now.cshnw.databinding.FragmentRecipesBinding
import cash.now.cshnw.utils.NetworkListener
import cash.now.cshnw.utils.NetworkResult
import cash.now.cshnw.utils.observeOnce
import cash.now.cshnw.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var networkListener: NetworkListener

    //    private lateinit var mview: View
    private val recipesAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        /** we added this because we gonna use life data objects
         * noted that it's not exist in android documentation */
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)


        setupRecyclerView()
        //requestApiData()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
            binding.booleanValue =it
        })


        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.d("NetworkListener", status.toString())
                recipesViewModel.networkState = status
                recipesViewModel.showNetworkState()
                readDatabse()
            }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkState) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkState()
            }
        }
        return binding.root
    }

    private fun readDatabse() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner, { database ->

                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.v("recipesFragment", "readDatabse called!")
                    recipesAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }

            })
        }
    }


    private fun requestApiData() {
        Log.v("recipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQuery())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }

                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }


    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recipesAdapter
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

        //  super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            val text = query.trim()
            searchApiData(text)
        }
        hideKeyboard()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {

        return true
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchRecipeResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { recipesAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        })
    }

    private fun loadDataCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    recipesAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun loseConnectionBar(status: Boolean) {
        if (status) {
            binding.internetCardView.visibility = View.GONE
        } else {
            binding.internetCardView.visibility = View.VISIBLE
        }
    }

    private fun showShimmerEffect() {
        binding.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerview.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}