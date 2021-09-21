package com.example.foodyapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodyapp.data.DataStoreRepository
import com.example.foodyapp.utils.Constants.Companion.API_KEY
import com.example.foodyapp.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodyapp.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodyapp.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foodyapp.utils.Constants.Companion.QUERY_ADD_RECIPES_INFORMATION
import com.example.foodyapp.utils.Constants.Companion.QUERY_API_KEY
import com.example.foodyapp.utils.Constants.Companion.QUERY_DIET
import com.example.foodyapp.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foodyapp.utils.Constants.Companion.QUERY_SEARCH
import com.example.foodyapp.utils.Constants.Companion.QUERY_NUMBER
import com.example.foodyapp.utils.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE
    var networkState = false
    var backOnline = false

    val readDataStoreMealAndDietType = dataStoreRepository.readFromDataStore
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveBackOnline (value: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(value)
        }
    }


    fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
    }


    fun applySearchQuery(searchQuery : String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readDataStoreMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkState() {
        if (!networkState) {
            Toast.makeText(getApplication(), "No internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else if (networkState){
            if (backOnline){
                Toast.makeText(getApplication(), "We are Back Online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}