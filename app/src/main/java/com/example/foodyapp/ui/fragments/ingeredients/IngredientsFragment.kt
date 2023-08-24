package cash.now.cshnw.ui.fragments.ingeredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cash.now.cshnw.R
import cash.now.cshnw.adapters.IngredientsAdapter
import cash.now.cshnw.models.Result
import cash.now.cshnw.utils.Constants.Companion.RECIPES_RESULT


class IngredientsFragment : Fragment() {

    private val ingredientsAdapter : IngredientsAdapter by lazy {
        IngredientsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingeredients, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPES_RESULT)

        setupRecyclerVIew(view)

        myBundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }


        return view
    }

    fun setupRecyclerVIew(view : View){
        val recycler = view.findViewById<RecyclerView>(R.id.ingredient_recycler_View)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ingredientsAdapter
    }

}