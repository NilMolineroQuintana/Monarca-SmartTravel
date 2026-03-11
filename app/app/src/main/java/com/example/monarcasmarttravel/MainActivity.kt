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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.monarcasmarttravel.data.repository.PreferencesRepository
import com.example.monarcasmarttravel.ui.screens.AboutUsScreen
import com.example.monarcasmarttravel.ui.screens.AlbumScreen
import com.example.monarcasmarttravel.ui.screens.CreateTripScreen
import com.example.monarcasmarttravel.ui.screens.HomeScreen
import com.example.monarcasmarttravel.ui.screens.ItineraryScreen
import com.example.monarcasmarttravel.ui.screens.LoginScreen
import com.example.monarcasmarttravel.ui.screens.PlanOptionsScreen
import com.example.monarcasmarttravel.ui.screens.PlanScreen
import com.example.monarcasmarttravel.ui.screens.ProfileScreen
import com.example.monarcasmarttravel.ui.screens.SplashScreen
import com.example.monarcasmarttravel.ui.screens.TermsAndConditionsScreen
import com.example.monarcasmarttravel.ui.screens.TripsScreen
import com.example.monarcasmarttravel.ui.theme.MonarcaSmartTravelTheme
import com.example.monarcasmarttravel.ui.theme.ThemeState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val prefsRepository = PreferencesRepository(this)
        LanguageChangeUtil().changeLanguage(this, prefsRepository.language)

        ThemeState.isDarkMode = prefsRepository.isDarkMode

        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MonarcaSmartTravelTheme(darkTheme = ThemeState.isDarkMode) {
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
            startDestination = "splash",
        ) {
            composable("splash") {
                SplashScreen(onTimeout = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("login") {
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
                route = "termsAndConditions?isLoginScreen={isLoginScreen}",
                arguments = listOf(
                    navArgument("isLoginScreen") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val isLoginScreen = backStackEntry.arguments?.getBoolean("isLoginScreen") ?: false
                TermsAndConditionsScreen(navController, isLoginScreen)
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