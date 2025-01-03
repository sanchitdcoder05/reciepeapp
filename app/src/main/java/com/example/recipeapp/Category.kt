package com.example.recipeapp

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class CategoriesResponse(
    val categories: List<Category>
)

data class Dish(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strYoutube: String,
)

data class DishesResponse(
    val dishes: List<Dish>
)

data class RecipeResponse(val meals: List<Recipe>?)

data class Recipe(
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strYoutube: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
)
