package com.example.monarcasmarttravel.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.monarcasmarttravel.R

@Composable
fun MyBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem (
            selected = currentRoute == "home",
            onClick = { if (currentRoute != "home") navController.navigate("home") },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_home)) }
        )
        NavigationBarItem(
            selected = currentRoute == "trips",
            onClick = {if (currentRoute != "trips") navController.navigate("trips") },
            icon = { Icon(imageVector = Icons.Filled.Luggage, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_trips)) }
        )
        NavigationBarItem(
            selected = currentRoute == "create",
            onClick = { if (currentRoute != "create") navController.navigate("create") },
            icon = { Icon(imageVector = Icons.Filled.AddCircle, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_create)) }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { if (currentRoute != "profile") navController.navigate("profile") },
            icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_profile)) }
        )
    }
}