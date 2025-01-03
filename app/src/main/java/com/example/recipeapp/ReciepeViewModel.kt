package com.example.recipeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
