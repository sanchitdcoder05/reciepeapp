package com.example.recipeapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val _categoriestate = mutableStateOf(ReciepeState())
    val categoriestate: State<ReciepeState> = _categoriestate

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCategories()
                _categoriestate.value = _categoriestate.value.copy(
                    list = response.categories,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _categoriestate.value = _categoriestate.value.copy(
                    error = "Error fetching Categories: ${e.message}",
                    loading = false
                )
            }
        }
    }

    data class ReciepeState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )
}
