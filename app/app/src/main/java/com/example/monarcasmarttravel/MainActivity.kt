package com.example.monarcasmarttravel

import CreateScreen
import HomeScreen
import ProfileScreen
import TripsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.ui.theme.MonarcaSmartTravelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonarcaSmartTravelTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("trips") {
            TripsScreen(navController)
        }
        composable("create") {
            CreateScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
    }
}