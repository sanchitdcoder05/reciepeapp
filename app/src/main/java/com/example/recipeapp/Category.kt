package com.example.recipeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String,
): Parcelable

data class CategoriesResponse(
    val categories: List<Category>
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


data class MealResponse(
    val meals: List<Meal>
)

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strDrinkAlternate: String?,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strTags: String?,
    val strYoutube: String,
    val strIngredients: List<String?>,
    val strMeasures: List<String?>
)


data class IngredientResponse(
    val meals: List<IngredientDish>?
)

data class IngredientDish(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String
)



