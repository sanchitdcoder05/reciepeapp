package com.example.recipeapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, mealName: String = ""){
    val viewModel: RecipeViewModel = viewModel()
    val recipe by viewModel.recipe.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (mealName != null){
            query = mealName
            viewModel.fetchRecipe(query)
        }
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    if (query != ""){
                        Row {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(query)
                        }
                    }
                    else{
                        Row {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Search Recipe")
                        }
                    }
                },
                navigationIcon = {
                    if (query != "") {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.pointerInput(Unit) {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            )
        }
        ,
        bottomBar = {
            BottomAppBar {
                var selectedTab by remember { mutableStateOf("Search") }
                NavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    navController = navController
                )
            }
        },
        content = { paddingValues ->


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ){
            Column(
                modifier = Modifier
                    .padding(12.dp)

            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.fetchRecipe(query)
                    },
                    label = { Text("Search Recipe") },
                    trailingIcon = { Icon(Icons.Filled.ArrowForward, contentDescription = "Search Icon", modifier = Modifier.clickable {
                        viewModel.fetchRecipe(query)
                    }) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Disclaimer : Put Popular Dishes Only")
                Spacer(modifier = Modifier.height(16.dp))

                if (recipe != null && query != "") {
                    RecipeDetails(recipe = recipe!!)
                } else if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        "No recipe found. Please search!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        }
    )
}


@Composable
fun RecipeDetails(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(recipe.strMeal)
        Image(
            painter = rememberAsyncImagePainter(recipe.strMealThumb),
            contentDescription = recipe.strMeal,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Category: ${recipe.strCategory}")
        Text("Area: ${recipe.strArea}")
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text("Instructions : ")
            Text("Youtube", color = Color.Red, modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.strYoutube))
                context.startActivity(intent)
            }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(recipe.strInstructions)
    }
}