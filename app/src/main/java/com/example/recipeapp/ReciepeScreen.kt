package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    viewState: CategoryViewModel.ReciepeState,
    navigateToDetail: (Category) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Categories") })
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
                    viewState.loading -> CircularProgressIndicator()
                    viewState.error != null -> Text(text = "Error: ${viewState.error}", color = Color.Red)
                    viewState.list.isEmpty() -> Text(text = "No categories available.", color = Color.Gray)
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(viewState.list) { category ->
                                CategoryItem(
                                    category = category,
                                    navigateToDetail = navigateToDetail
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
    navigateToDetail: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .fillMaxSize()
            .clickable { navigateToDetail(category) }
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DishItem(category: Category, navController: NavController) {
    val context = LocalContext.current
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = category.strCategory) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Menu") }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)

            ) {
                Column (
                    modifier = Modifier.fillMaxSize().border(2.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp)).padding(15.dp)
                ){
                    Image(
                        painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).size(250.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(19.dp))

                    Column (
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(color = Color.DarkGray).padding(12.dp)
                    ){
                        Text(category.strCategoryDescription, textAlign = TextAlign.Justify)
                    }
                }
            }
        }
    )
}
