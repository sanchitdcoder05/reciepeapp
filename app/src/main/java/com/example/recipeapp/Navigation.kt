package com.example.recipeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun NavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NavigationBarItem(
            selected = (selectedTab == "Home"),
            onClick = {
                onTabSelected("Home")
                navController.navigate("Home")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home Icon"
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color(0xFF87CEEB),
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = Color(0xFF87CEEB)
            )
        )


        NavigationBarItem(
            selected = (selectedTab == "Search"),
            onClick = {
                onTabSelected("Search")
                navController.navigate("SearchScreen") { // ✅ Corrected typo
                    popUpTo("Home") { inclusive = false } // Optional: Prevents back stack issues
                }
            },
            icon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
            label = { Text("Search") }
        )

        NavigationBarItem(
            selected = (selectedTab == "Menu"),
            onClick = {
                onTabSelected("Menu")
                navController.navigate("CategoryScreen") {
                    popUpTo("Home") { inclusive = false }
                }
            },
            icon = { Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon") },
            label = { Text("Menu") }
        )
    }
}
