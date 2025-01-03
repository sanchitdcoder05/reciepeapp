package com.example.recipeapp

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DishViewModel : ViewModel() {
    private val _dishestate = MutableStateFlow(DishState())
    val dishestate: StateFlow<DishState> get() = _dishestate

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun fetchDishes(categoryName: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDishes(categoryName)
                if (response.dishes.isNotEmpty()) {
                    _dishestate.value = DishState(
                        list = response.dishes,
                        loading = false,
                        error = null
                    )
                    _errorMessage.value = ""
                } else {
                    _dishestate.value = DishState(
                        list = emptyList(),
                        loading = false,
                        error = "No dishes found for \"$categoryName\"."
                    )
                    _errorMessage.value = "No dishes found for \"$categoryName\"."
                }
            } catch (e: Exception) {
                _dishestate.value = DishState(
                    list = emptyList(),
                    loading = false,
                    error = "Failed to load dishes: ${e.localizedMessage}"
                )
                _errorMessage.value = "Failed to load dishes: ${e.localizedMessage}"
            }
        }
    }

    data class DishState(
        val loading: Boolean = true,
        val list: List<Dish> = emptyList(),
        val error: String? = null
    )
}
