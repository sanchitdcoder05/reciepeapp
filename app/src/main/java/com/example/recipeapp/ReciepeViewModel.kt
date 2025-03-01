package com.example.recipeapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun fetchRecipe(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchRecipe(query)
                if (!response.meals.isNullOrEmpty()) {
                    _recipe.value = response.meals.firstOrNull()
                    _errorMessage.value = ""
                } else {
                    _recipe.value = null
                    _errorMessage.value = "No recipe found for \"$query\"."
                }
            } catch (e: Exception) {
                _recipe.value = null
                _errorMessage.value = "Failed to load recipe: ${e.localizedMessage}"
            }
        }
    }
}

class ListViewModel : ViewModel() {
    private val _mealsByLetter = MutableStateFlow<Map<String, List<Meal>>>(emptyMap())
    val mealsByLetter: StateFlow<Map<String, List<Meal>>> = _mealsByLetter

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchMealsByFirstLetter(letter: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMealsByFirstLetter(letter)
                if (!response.meals.isNullOrEmpty()) {
                    _mealsByLetter.value = _mealsByLetter.value.toMutableMap().apply {
                        put(letter, response.meals)
                    }
                    _errorMessage.value = ""
                } else {
                    _mealsByLetter.value = _mealsByLetter.value.toMutableMap().apply {
                        put(letter, emptyList())
                    }
                    _errorMessage.value = "No meals found for \"$letter\"."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load meals: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}


class IngredientViewModel : ViewModel() {
    private val _ingredientMeals = MutableStateFlow<Map<String, List<Meal>>>(emptyMap())
    val ingredientMeals: StateFlow<Map<String, List<Meal>>> = _ingredientMeals

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchDishesByIngredient(ingredient: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMealsByIngredient(ingredient)
                if (response.meals.isNullOrEmpty()) {
                    _ingredientMeals.value = _ingredientMeals.value.toMutableMap().apply {
                        put(ingredient, emptyList())
                    }
                    _errorMessage.value = "No dishes found for \"$ingredient\"."
                } else {
                    _ingredientMeals.value = _ingredientMeals.value.toMutableMap().apply {
                        put(ingredient, response.meals ?: emptyList())
                    }
                    _errorMessage.value = ""
                }
            } catch (e: Exception) {
                _ingredientMeals.value = _ingredientMeals.value.toMutableMap().apply {
                    put(ingredient, emptyList())
                }
                _errorMessage.value = "Failed to load dishes: ${e.localizedMessage}"
                Log.e("IngredientViewModel", "Error fetching dishes: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
