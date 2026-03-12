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
import com.example.monarcasmarttravel.data.repository.PreferencesManager
import com.example.monarcasmarttravel.ui.screens.preferences.AboutUsScreen
import com.example.monarcasmarttravel.ui.screens.preferences.ProfileScreen
import com.example.monarcasmarttravel.ui.screens.preferences.TermsAndConditionsScreen
import com.example.monarcasmarttravel.ui.screens.start.HomeScreen
import com.example.monarcasmarttravel.ui.screens.start.LoginScreen
import com.example.monarcasmarttravel.ui.screens.start.SplashScreen
import com.example.monarcasmarttravel.ui.screens.trip.AlbumScreen
import com.example.monarcasmarttravel.ui.screens.trip.CreateTripScreen
import com.example.monarcasmarttravel.ui.screens.trip.ItineraryScreen
import com.example.monarcasmarttravel.ui.screens.trip.PlanOptionsScreen
import com.example.monarcasmarttravel.ui.screens.trip.PlanScreen
import com.example.monarcasmarttravel.ui.screens.trip.TripsScreen
import com.example.monarcasmarttravel.ui.theme.MonarcaSmartTravelTheme
import com.example.monarcasmarttravel.ui.theme.ThemeState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        ThemeState.isDarkMode = preferencesManager.isDarkMode

        enableEdgeToEdge()
        setContent {
            MonarcaSmartTravelTheme(darkTheme = ThemeState.isDarkMode) {
                AppNavigation()
            }
        }
    }
}

/**
 * Navegació principal de l'aplicació.
 *
 * El [TripViewModel] ja no es crea manualment amb remember{}: ara Hilt
 * l'injecta a cada pantalla via hiltViewModel(), garantint que el cicle
 * de vida sigui correcte i que l'estat no es perdi en rotar la pantalla.
 */
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
            composable(
                route = "album/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: 1
                AlbumScreen(navController, tripId)
            }
            composable(
                route = "plan/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: 1
                PlanOptionsScreen(navController, tripId)
            }
            composable(
                route = "plan/{route}/{tripId}",
                arguments = listOf(
                    navArgument("route") { type = NavType.StringType },
                    navArgument("tripId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val ruta = backStackEntry.arguments?.getString("route")
                val tripId = backStackEntry.arguments?.getInt("tripId") ?: 1
                PlanScreen(navController, ruta, tripId)
            }
            composable("createTrip") {
                CreateTripScreen(navController)
            }
        }
    }
}