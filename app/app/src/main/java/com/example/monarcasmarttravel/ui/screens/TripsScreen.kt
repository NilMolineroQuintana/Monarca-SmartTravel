package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.TripCard
import com.example.monarcasmarttravel.ui.viewmodel.TripViewModel

/**
 * Pantalla que mostra la llista de viatges de l'usuari.
 *
 * Observa el [TripViewModel]: qualsevol operació CRUD es reflecteix
 * automàticament gràcies a mutableStateOf. La llista comença buida;
 * l'usuari ha de crear els seus propis viatges.
 *
 * @param navController Controlador de navegació.
 * @param viewModel ViewModel compartit injectat per Compose.
 */
@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostra errors del ViewModel com a Snackbar
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.trips_text)) },
        bottomBar = { MyBottomBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("createTrip") }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        if (viewModel.trips.isEmpty()) {
            // Estat buit: guia l'usuari a crear el primer viatge
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(AppDimensions.PaddingLarge),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✈️",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = stringResource(R.string.no_trips),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = AppDimensions.PaddingMedium)
                )
                Text(
                    text = stringResource(R.string.no_trips_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = AppDimensions.PaddingSmall)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
                contentPadding = PaddingValues(
                    start = AppDimensions.PaddingMedium,
                    end = AppDimensions.PaddingMedium,
                    top = AppDimensions.PaddingSmall,
                    bottom = AppDimensions.PaddingLarge
                )
            ) {
                items(viewModel.trips, key = { it.id }) { trip ->
                    TripCard(
                        trip = trip,
                        onClick = { navController.navigate("itinerary/${trip.id}") }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    TripsScreen(rememberNavController(), viewModel())
}