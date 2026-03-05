package com.example.monarcasmarttravel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.monarcasmarttravel.ui.screens.AboutUsScreen
import com.example.monarcasmarttravel.ui.screens.AlbumScreen
import com.example.monarcasmarttravel.ui.screens.CreateTripScreen
import com.example.monarcasmarttravel.ui.screens.HomeScreen
import com.example.monarcasmarttravel.ui.screens.ItineraryScreen
import com.example.monarcasmarttravel.ui.screens.LoginScreen
import com.example.monarcasmarttravel.ui.screens.PlanOptionsScreen
import com.example.monarcasmarttravel.ui.screens.PlanScreen
import com.example.monarcasmarttravel.ui.screens.ProfileScreen
import com.example.monarcasmarttravel.ui.screens.TermsAndConditionsScreen
import com.example.monarcasmarttravel.ui.screens.TripsScreen
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
            startDestination = "termsAndConditions?firstTime=true",
        ) {
            composable ("login") {
                LoginScreen(navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
            composable("trips") {
                TripsScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("aboutUs") {
                AboutUsScreen(navController)
            }
            composable(
                route = "termsAndConditions?firstTime={firstTime}",
                arguments = listOf(
                    navArgument("firstTime") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val isFirstTime = backStackEntry.arguments?.getBoolean("firstTime") ?: false

                TermsAndConditionsScreen(navController, isFirstTime)
            }
            composable(
                route = "itinerary/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: 1
                ItineraryScreen(navController, tripId)
            }
            composable("plan") {
                PlanOptionsScreen(navController)
            }
            composable(
                route = "album/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: 1
                AlbumScreen(navController, tripId)
            }
            composable(
                route = "plan/{route}",
                arguments = listOf(navArgument("route") { type = NavType.StringType })
            ) { backStackEntry ->
                val ruta = backStackEntry.arguments?.getString("route")
                PlanScreen(navController, ruta)
            }
            composable("createTrip") {
                CreateTripScreen(navController)
            }
        }
    }
}