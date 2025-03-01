package com.example.recipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()
                val categoryViewModel: CategoryViewModel = viewModel()
                val viewState = categoryViewModel.categoriestate.value

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "Home",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("Home") {
                            Home(navController = navController)
                        }

                        composable(route = Screen.CategoryScreen.route) {
                            CategoryScreen(
                                navController = navController,
                                viewState = viewState,
                                navigateToDetail = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("cat", it)
                                    navController.navigate(Screen.DishItem.route)
                                }
                            )
                        }

                        composable(route = Screen.DishItem.route) {
                            val category = navController.previousBackStackEntry?.savedStateHandle?.get<Category>("cat")
                                ?: Category("", "", "", "")
                            DishItem(category = category, navController = navController)
                        }

                        composable("SearchScreen") {
                            SearchScreen(navController)
                        }

                        composable("SearchScreen/{mealName}") { backStackEntry ->
                            val mealName = backStackEntry.arguments?.getString("mealName") ?: ""
                            SearchScreen(navController, mealName)
                        }

                    }
                }
            }
        }
    }
}
