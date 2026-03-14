package com.example.monarcasmarttravel.ui.screens.trip

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
import androidx.compose.material.icons.filled.Info
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.DateField
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodels.TripViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "CreateTripScreen"
private const val DATE_FORMAT = "dd/MM/yyyy"

/**
 * Pantalla per crear un nou viatge.
 *
 * La UI valida que tots els camps obligatoris tinguin contingut i que
 * la data de fi sigui posterior a la d'inici, mostrant errors en línia
 * abans d'habilitar el botó (validació capa UI, requerida pel lab).
 *
 * La validació de negoci final la fa el repositori a través del ViewModel,
 * i els errors pugen com a [errorMessage] mostrats en un Snackbar.
 *
 * @param navController Controlador de navegació.
 * @param viewModel ViewModel gestionat per Hilt.
 */
@Composable
fun CreateTripScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    val sdf = remember { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }

    var title       by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDateText by remember { mutableStateOf("") }
    var endDateText   by remember { mutableStateOf("") }
    var startDate     by remember { mutableStateOf<Date?>(null) }
    var endDate       by remember { mutableStateOf<Date?>(null) }

    // Errors en línia per a cada camp (validació capa UI)
    var titleError       by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var dateRangeError   by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostra els errors del domini/repositori com a Snackbar
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // Validació en línia de les dates: la fi ha de ser posterior a l'inici
    LaunchedEffect(startDate, endDate) {
        if (startDate != null && endDate != null && !endDate!!.after(startDate)) {
            dateRangeError = "La data de fi ha de ser posterior a la d'inici."
        } else {
            dateRangeError = null
        }
    }

    // El botó s'habilita només quan tots els camps són vàlids (validació capa UI)
    val isFormValid = title.isNotBlank()
            && description.isNotBlank()
            && startDate != null
            && endDate != null
            && dateRangeError == null

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

            // ── Camp títol ───────────────────────────────────────────────────
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    // Neteja l'error quan l'usuari comença a escriure
                    if (it.isNotBlank()) titleError = null
                },
                label = { Text(stringResource(R.string.destination)) },
                placeholder = { Text(stringResource(R.string.example_destination)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = titleError != null,
                supportingText = {
                    if (titleError != null) Text(titleError!!, color = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ── Camp descripció ──────────────────────────────────────────────
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    if (it.isNotBlank()) descriptionError = null
                },
                label = { Text(stringResource(R.string.description)) },
                placeholder = { Text(stringResource(R.string.description_example, title)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = descriptionError != null,
                supportingText = {
                    if (descriptionError != null) Text(descriptionError!!, color = MaterialTheme.colorScheme.error)
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
                    },
                    showTime = false
                )
                DateField(
                    value = endDateText,
                    label = stringResource(R.string.final_date),
                    modifier = Modifier.weight(1f),
                    onDateSelected = { dateStr ->
                        endDateText = dateStr
                        endDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                        Log.d(TAG, "Data fi seleccionada: $dateStr")
                    },
                    showTime = false
                )
            }

            // Missatge d'error en línia per al rang de dates (validació capa UI)
            if (dateRangeError != null) {
                Text(
                    text = dateRangeError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Botó de crear ────────────────────────────────────────────────
            Button(
                onClick = {
                    // Marcar camps buits amb error (validació capa UI)
                    var hasError = false
                    if (title.isBlank()) {
                        titleError = "La destinació no pot estar buida."
                        hasError = true
                    }
                    if (description.isBlank()) {
                        descriptionError = "La descripció no pot estar buida."
                        hasError = true
                    }
                    if (hasError) return@Button

                    val success = viewModel.addTrip(
                        title = title.trim(),
                        description = description,
                        dateIn = startDate!!,
                        dateOut = endDate!!,
                        userId = 1 // TODO: substituir per l'ID de l'usuari autenticat
                    )
                    if (success) {
                        Log.i(TAG, "Viatge creat correctament -> destí=$title")
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
    CreateTripScreen(rememberNavController())
}