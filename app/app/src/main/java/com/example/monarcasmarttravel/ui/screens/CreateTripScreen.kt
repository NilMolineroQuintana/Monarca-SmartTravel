package com.example.monarcasmarttravel.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.DateField
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodel.TripViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "CreateTripScreen"
private const val DATE_FORMAT = "dd/MM/yyyy"

/**
 * Pantalla per crear un nou viatge.
 *
 * La UI només comprova que els camps no estiguin buits per habilitar
 * el botó (feedback visual immediat). La validació real de les dades
 * (destinació en blanc, dates incoherents) la fa [Trip.createTrip] a
 * través del domini, i els errors pugen al ViewModel com a [errorMessage]
 * i es mostren com a Snackbar.
 *
 * @param navController Controlador de navegació.
 * @param viewModel ViewModel compartit per a la gestió de viatges.
 */
@Composable
fun CreateTripScreen(
    navController: NavController,
    viewModel: TripViewModel
) {
    val sdf = remember { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }

    var destination   by remember { mutableStateOf("") }
    var startDateText by remember { mutableStateOf("") }
    var endDateText   by remember { mutableStateOf("") }
    var startDate     by remember { mutableStateOf<Date?>(null) }
    var endDate       by remember { mutableStateOf<Date?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostra els errors del domini com a Snackbar
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // El botó s'habilita quan tots els camps tenen contingut
    val isFormValid = destination.isNotBlank()
            && startDate != null
            && endDate != null

    Scaffold(
        topBar = {
            MyTopBar(
                title = stringResource(R.string.new_trip),
                onBackClick = { navController.popBackStack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.itinerary_details),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // ── Camp destinació ──────────────────────────────────────────────
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text(stringResource(R.string.destination)) },
                placeholder = { Text(stringResource(R.string.example_destination)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ── Camps de data amb DateField (Material3 DatePicker) ───────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
                modifier = Modifier.fillMaxWidth()
            ) {
                DateField(
                    value = startDateText,
                    label = stringResource(R.string.start_date),
                    modifier = Modifier.weight(1f),
                    onDateSelected = { dateStr ->
                        startDateText = dateStr
                        startDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                        Log.d(TAG, "Data inici seleccionada: $dateStr")
                    }
                )
                DateField(
                    value = endDateText,
                    label = stringResource(R.string.final_date),
                    modifier = Modifier.weight(1f),
                    onDateSelected = { dateStr ->
                        endDateText = dateStr
                        endDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                        Log.d(TAG, "Data fi seleccionada: $dateStr")
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Botó de crear ────────────────────────────────────────────────
            Button(
                onClick = {
                    // La validació la fa Trip.createTrip()
                    val success = viewModel.addTrip(
                        destination = destination.trim(),
                        dateIn = startDate!!,
                        dateOut = endDate!!,
                        userId = 1 // TODO: substituir per l'ID de l'usuari autenticat
                    )
                    if (success) {
                        Log.i(TAG, "Viatge creat correctament -> destí=$destination")
                        navController.navigate("trips") {
                            popUpTo("trips") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.create_trip),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTripScreenPreview() {
    CreateTripScreen(rememberNavController(), viewModel())
}