package com.example.yumyum.ui.secondactivity

import android.util.Log
import androidx.annotation.LongDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.model.ReturnedMeal
import com.example.yumyum.data.model.SearchedMeal
import com.example.yumyum.data.model.relation.UserMealCrossRef
import com.example.yumyum.data.repository.MealsRepository
import com.example.yumyum.data.repository.UserRepository
import com.example.yumyum.ui.Resource
import com.example.yumyum.ui.firstactivity.UserViewModel
import com.example.yumyum.util.Constant
import com.example.yumyum.util.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@OptIn(FlowPreview::class)
class MealViewModel(
    private val repo:MealsRepository
):ViewModel() {
    private val _areas = MutableLiveData<Resource<Areas>>()
    val areas:LiveData<Resource<Areas>> get() = _areas

    private val _categories = MutableLiveData<Resource<Categories>>()
    val categories:LiveData<Resource<Categories>> get() = _categories

    private val _meals = MutableLiveData<Resource<List<Meal>>>()
    val meals:LiveData<Resource<List<Meal>>> get() = _meals

    private val _favMeals = MutableLiveData<Resource<List<FavoriteMeal>>>()
    val favMeals:LiveData<Resource<List<FavoriteMeal>>> get() = _favMeals

    private val _searchResults = MutableLiveData<Resource<SearchedMeal>>()
    val searchResults: LiveData<Resource<SearchedMeal>> get() = _searchResults

    private val queryChannel = Channel<String>(Channel.CONFLATED)

    private val _mealDetails = MutableLiveData<Resource<ReturnedMeal>>()
    val mealDetails: LiveData<Resource<ReturnedMeal>> get() = _mealDetails

    private val _favoriteMealIds = MutableLiveData<Set<String>>()
    val favoriteMealIds: LiveData<Set<String>> get() = _favoriteMealIds

    init {
        viewModelScope.launch(Dispatchers.IO) {
            queryChannel.receiveAsFlow()
                .debounce(800)
                .distinctUntilChanged()
                .collect { query ->
                    searchMealByName(query)
                }
        }
    }
    fun getAllAreas(){
        viewModelScope.launch (Dispatchers.IO){
            _areas.postValue(Resource.Loading())
            try {
                val result = repo.getAreas()
                _areas.postValue(Resource.Success(result))
            } catch (e: Exception) {
                _areas.postValue(Resource.Error("Error retrieving areas: ${e.message}"))
                Log.e("MealViewModel", "Error retrieving all areas: ${e.message}")
            }
        }
    }

    fun getAllCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            _categories.postValue(Resource.Loading())
            try {
                val result = repo.getCategories()
                _categories.postValue(Resource.Success(result))
            } catch (e: Exception) {
                _categories.postValue(Resource.Error("Error retrieving categories: ${e.message}"))
                Log.e("MealViewModel", "Error retrieving all categories: ${e.message}")
            }
        }
    }

    fun getMeals(term: String,filterType:Int){
        viewModelScope.launch(Dispatchers.IO) {
            _meals.postValue(Resource.Loading())
            try {
                if (filterType == FilterType.AREA.ordinal) {
                    val result = repo.getMealsByAreas(term)
                    _meals.postValue(Resource.Success(result))
                }else{
                    val result = repo.getMealsByCategories(term)
                    _meals.postValue(Resource.Success(result))
                }
            } catch (e: Exception) {
                _meals.postValue(Resource.Error("Error retrieving meals: ${e.message}"))
                Log.e("MealViewModel", "Error retrieving meals: ${e.message}")
            }

        }
    }

    fun insertFavoriteMealById(username:String,mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { repo.getMealById(mealId) }
                val meal = result.await()
                Log.d("checking_database", meal.toString())
                repo.insertFavoriteMeal(meal.meals[0])
                val crossRef = UserMealCrossRef(username, meal.meals[0].idMeal)
                repo.insertCrossRef(crossRef)
            }catch (e: Exception) {
            Log.e("MealViewModel", "Error inserting favorites: ${e.message}")
            }
        }
    }

    fun getFavoriteMealsByUsername(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            _favMeals.postValue(Resource.Loading())
            try {
                val meals = repo.getMealsByUsername(username)
                _favMeals.postValue(Resource.Success(meals))
            } catch (e: Exception) {
                _favMeals.postValue(Resource.Error("Error retrieving favorite meals: ${e.message}"))
                Log.e("MealViewModel", "Error retrieving favorites: ${e.message}")
            }
        }
    }

    fun searchMealByName(mealName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.postValue(Resource.Loading())
            try {
                val result = repo.searchMealByName(mealName)
                _searchResults.postValue(Resource.Success(result))
            } catch (e: Exception) {
                _searchResults.postValue(Resource.Error("Error searching for meal: ${e.message}"))
                Log.e("MealViewModel", "Error searching for meal: ${e.message}")
            }
        }
    }
    fun submitQuery(query: String) {
        queryChannel.trySend(query)
    }
    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _mealDetails.postValue(Resource.Loading())
            try {
                val result = repo.getMealById(mealId)
                _mealDetails.postValue(Resource.Success(result))

            } catch (e: UnknownHostException) {
                _mealDetails.postValue(Resource.Error("Error fetching meal details: ${e.message}"))
                Log.e("MealViewModel", "Error fetching meal details: ${e.message}")
            }
        }
    }
    fun deleteMealFromFavorites(username: String, mealId: String)
    {
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = async { repo.getMealById(mealId) }
                val meal = result.await()
                repo.deleteFavoriteMeal(meal.meals[0])
                repo.deleteCrossRef(username,meal.meals[0].idMeal)
                getFavoriteMealsByUsername(username)
            } catch (e: UnknownHostException) {
                Log.e("MealViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun getFavoriteMealIds() {
        viewModelScope.launch {
            try {
                val ids = repo.getFavoriteMealIdsByUsername(Constant.USER_NAME)
                _favoriteMealIds.postValue(ids.toSet())
            } catch (e: UnknownHostException) {
                Log.e("MealViewModel", "Error fetching favorite meal ids: ${e.message}")
            }
        }
    }

}

class MealViewModelFactory(
    private val repo: MealsRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(repo) as T
    }
}