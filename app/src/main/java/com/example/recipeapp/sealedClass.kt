package com.example.recipeapp

sealed class Screen(val route:String) {
    object CategoryScreen:Screen("CategoryScreen")
    object DishItem:Screen("DishScreen")
}