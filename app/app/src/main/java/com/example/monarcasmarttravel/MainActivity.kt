package com.example.monarcasmarttravel

import HomeScreen
import NotificationsScreen
import PreferencesScreen
import ProfileScreen
import TripsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("trips") {
                TripsScreen(navController)
            }
            composable("notifications") {
                NotificationsScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("preferences") {
                PreferencesScreen(navController)
            }
        }
    }
}