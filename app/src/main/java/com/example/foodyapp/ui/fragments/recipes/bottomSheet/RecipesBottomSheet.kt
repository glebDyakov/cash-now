package com.example.foodyapp.ui.fragments.recipes.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodyapp.R
import com.example.foodyapp.databinding.FragmentRecipesBinding
import com.example.foodyapp.databinding.RecipesBottomSheetBinding
import com.example.foodyapp.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodyapp.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {
    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!
    lateinit var recipesViewModel: RecipesViewModel

    private var mealChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietChip = DEFAULT_MEAL_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        recipesViewModel.readDataStoreMealAndDietType.asLiveData()
            .observe(viewLifecycleOwner, { value ->
                mealChip = value.selectedMealType
                dietChip = value.selectDietType
                updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
                updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
            })


        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedChip = chip.text.toString().toLowerCase(Locale.ROOT)
            mealChip = selectedChip
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietChip = chip.text.toString().toLowerCase(Locale.ROOT)
            dietChip = selectedDietChip
            dietTypeChipId = selectedChipId
        }

        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(mealChip, mealTypeChipId, dietChip, dietTypeChipId)
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)

        }

        return binding.root
    }

    private fun updateChip(selectMealType: Int, mealTypeChipGroup: ChipGroup) {
        if (selectMealType != 0) {
            try {
                mealTypeChipGroup.findViewById<Chip>(selectMealType).isChecked = true
            } catch (e: Exception) {
                Log.v("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}