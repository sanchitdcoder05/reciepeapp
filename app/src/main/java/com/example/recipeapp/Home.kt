package com.example.recipeapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val listViewModel: ListViewModel = viewModel()
    val ingredientViewModel: IngredientViewModel = viewModel()

    val errorMessage by listViewModel.errorMessage.collectAsState()
    val mealsByLetter by listViewModel.mealsByLetter.collectAsState()
    val ingredientMeals by ingredientViewModel.ingredientMeals.collectAsState()

    val isListLoading by listViewModel.isLoading.collectAsState()
    val isIngredientLoading by ingredientViewModel.isLoading.collectAsState()

    val isLoading = isListLoading || isIngredientLoading

    val firstLetters = listOf("A", "C", "D")
    val ingredients = listOf("Salmon", "Onion", "Chicken")

    LaunchedEffect(Unit) {
        firstLetters.forEach { letter ->
            listViewModel.fetchMealsByFirstLetter(letter)
        }

        ingredients.forEach { ingredient ->
            ingredientViewModel.fetchDishesByIngredient(ingredient)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Home") })
        },
        bottomBar = {
            BottomAppBar {
                var selectedTab by remember { mutableStateOf("Home") }
                NavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    navController = navController
                )
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                } else {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn {
                            ingredients.forEach { ingredient ->
                                val meals = ingredientMeals[ingredient]?.filter { it.strMeal.contains(ingredient, true) } ?: emptyList()

                                if (meals.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Meals with $ingredient",
                                            modifier = Modifier.padding(8.dp)
                                        )
                                        LazyRow {
                                            items(meals) { meal ->
                                                IngredientMealCard(meal = meal, navController)
                                            }
                                        }
                                    }
                                }
                            }

                            firstLetters.forEach { letter ->
                                val meals = mealsByLetter[letter] ?: emptyList()
                                if (meals.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Meals starting with '$letter'",
                                            modifier = Modifier.padding(8.dp)
                                        )
                                        LazyRow {
                                            items(meals) { meal ->
                                                MealCard(meal = meal, navController)
                                            }
                                        }
                                    }
                                } 
                            }
                        }
                    }
                }
            }
        }
    )
}




@Composable
fun IngredientMealCard(meal: Meal, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate("SearchScreen/${meal.strMeal}")
            }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(135.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Box(
                modifier = Modifier
                    .width(135.dp)
                    .padding(4.dp)
            ) {
                LazyRow {
                    item {
                        Text(text = meal.strMeal)
                    }
                }
            }
        }
    }
}


@Composable
fun MealCard(meal: Meal, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate("SearchScreen/${meal.strMeal}")
            }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(135.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Box(
                modifier = Modifier
                    .width(135.dp)
                    .padding(4.dp)
            ) {
                LazyRow {
                    item {
                        Text(text = meal.strMeal)
                    }
                }
            }
        }
    }
}
