package com.example.monarcasmarttravel

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.monarcasmarttravel.ui.screens.start.RecoverScreen
import com.example.monarcasmarttravel.ui.screens.start.RegisterScreen
import com.example.monarcasmarttravel.ui.screens.start.SplashScreen
import com.example.monarcasmarttravel.ui.screens.trip.AlbumScreen
import com.example.monarcasmarttravel.ui.screens.trip.CreateTripScreen
import com.example.monarcasmarttravel.ui.screens.trip.ItineraryScreen
import com.example.monarcasmarttravel.ui.screens.trip.PlanOptionsScreen
import com.example.monarcasmarttravel.ui.screens.trip.PlanScreen
import com.example.monarcasmarttravel.ui.screens.trip.TripsScreen
import com.example.monarcasmarttravel.ui.theme.MonarcaSmartTravelTheme
import com.example.monarcasmarttravel.ui.theme.ThemeState
import com.example.monarcasmarttravel.ui.viewmodels.AuthViewModel
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
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val user by authViewModel.user.collectAsState()

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
                    if (user != null) {
                        Log.d("MainActivity", "User: $user")
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                })
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("recoverPassword") {
                RecoverScreen(navController)
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
            ) {
                val isLoginScreen = it.arguments?.getBoolean("isLoginScreen") ?: false
                TermsAndConditionsScreen(navController, isLoginScreen)
            }
            composable(
                route = "itinerary/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) {
                val tripId = it.arguments?.getInt("tripId") ?: 1
                ItineraryScreen(navController, tripId)
            }
            composable(
                route = "album/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) {
                val tripId = it.arguments?.getInt("tripId") ?: 1
                AlbumScreen(navController, tripId)
            }
            composable(
                route = "createTrip?tripId={tripId}",
                arguments = listOf(
                    navArgument("tripId") {
                        type = NavType.IntType
                        defaultValue = -1   // -1 significa "no hi ha tripId" (mode creació)
                    }
                )
            ) {
                val rawId = it.arguments?.getInt("tripId") ?: -1
                val tripId = if (rawId == -1) null else rawId
                CreateTripScreen(navController, tripId)
            }
            // ── Plans de l'itinerari ─────────────────────────────────────────
            composable(
                route = "plan/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) {
                val tripId = it.arguments?.getInt("tripId") ?: 1
                PlanOptionsScreen(navController, tripId)
            }
            composable(
                route = "plan/{route}/{tripId}",
                arguments = listOf(
                    navArgument("route") { type = NavType.StringType },
                    navArgument("tripId") { type = NavType.IntType },
                )
            ) {
                val ruta = it.arguments?.getString("route")
                val tripId = it.arguments?.getInt("tripId") ?: 1
                PlanScreen(navController, ruta, tripId, null)
            }
            composable(
                route = "plan/{route}/{tripId}/{itemId}",
                arguments = listOf(
                    navArgument("route") { type = NavType.StringType },
                    navArgument("tripId") { type = NavType.IntType },
                    navArgument("itemId") { type = NavType.IntType }
                )
            ) {
                val ruta = it.arguments?.getString("route")
                val tripId = it.arguments?.getInt("tripId") ?: 1
                val itemId = it.arguments?.getInt("itemId")
                PlanScreen(navController, ruta, tripId, itemId)
            }
        }
    }
}