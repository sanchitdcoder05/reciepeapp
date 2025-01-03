package com.example.recipeapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController){
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Search") }
            )
        },
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
            val viewModel: RecipeViewModel = viewModel()
            val recipe by viewModel.recipe.collectAsState()
            val errorMessage by viewModel.errorMessage.collectAsState()
            var query by remember { mutableStateOf("") }

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
                    onValueChange = { query = it },
                    label = { Text("Search Recipe") },
                    trailingIcon = { Icon(Icons.Filled.ArrowForward, contentDescription = "Search Icon", modifier = Modifier.clickable {
                        viewModel.fetchRecipe(query)
                    }) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
//                Button(
//                    onClick = { viewModel.fetchRecipe(query) },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Search")
//                }

                Text("First letter should be capital and The Dish should be Polopu;alr Widely")
                Spacer(modifier = Modifier.height(16.dp))

                if (recipe != null) {
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
        Text("Instructions:")
        Spacer(modifier = Modifier.height(4.dp))
        Text("Watch on Youtube", modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.strYoutube))
            context.startActivity(intent)
        })
        Spacer(modifier = Modifier.height(8.dp))

        Text(recipe.strInstructions)
    }
}