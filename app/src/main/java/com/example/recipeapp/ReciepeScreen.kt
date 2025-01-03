package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.DishViewModel.DishState
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Categories") }
            )
        },
        bottomBar = {
            BottomAppBar {
                var selectedTab by remember { mutableStateOf("Menu") }
                NavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    navController = navController
                )
            }
        },
        content = { paddingValues ->
            val categoryViewModel: CategoryViewModel = viewModel()
            val viewState by categoryViewModel.categoriestate

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    viewState.loading -> {
                        CircularProgressIndicator()
                    }

                    viewState.error != null -> {
                        Text(
                            text = "Error: ${viewState.error}",
                            color = Color.Red
                        )
                    }

                    viewState.list.isEmpty() -> {
                        Text(
                            text = "No categories available.",
                            color = Color.Gray
                        )
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(viewState.list) { category ->
                                CategoryItem(
                                    category = category,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(
    category: Category,
    navController: NavController,
    dishViewModel: DishViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .fillMaxSize()
            .clickable {
                dishViewModel.fetchDishes(category.strCategory)
                navController.navigate("DishScreen")
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Text(
                text = category.strCategory,
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: DishViewModel = viewModel()
    val dishState by viewModel.dishestate.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text(text = "Dish Details") }
            )
        },
        bottomBar = {
            BottomAppBar {
                var selectedTab by remember { mutableStateOf("Menu") }
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
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    dishState.loading -> {
                        CircularProgressIndicator()
                    }
                    dishState.list.isNotEmpty() -> {
                        val dish = dishState.list.firstOrNull()
                        if (dish != null) {
                            DishItem(
                                dish = dish
                            )
                        } else {
                            Text(
                                text = "Unexpected error occurred while displaying the dish.",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    errorMessage.isNotEmpty() -> {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else -> {
                        Text(
                            text = "No recipe found. Please search!",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishItem(
    dish: Dish
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = dish.strMealThumb),
            contentDescription = "Dish Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(dish.strMeal, modifier = Modifier.size(100.dp))
        Spacer(modifier= Modifier.height(12.dp))

        val context = LocalContext.current
        Text(
            text = buildAnnotatedString {
                append("Youtube")
                addStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold), start = 0, end = 4)
            },
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dish.strYoutube))
                context.startActivity(intent)
            },
            fontSize = 18.sp
        )
        Spacer(modifier= Modifier.height(12.dp))

        Row {
            Column {
                Text(dish.strCategory)
            }
            Column {
                Text(dish.strArea)
            }
        }

        Spacer(modifier= Modifier.height(12.dp))

        Text(dish.strInstructions)

        Spacer(modifier= Modifier.height(12.dp))
    }
}
