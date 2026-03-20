package com.example.monarcasmarttravel.ui.screens.trip

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
 * Pantalla unificada per crear o editar un viatge
 *
 * - Si [tripId] és null → mode creació: formulari buit, crida [TripViewModel.addTrip].
 * - Si [tripId] té valor → mode edició: pre-omple els camps amb les dades actuals del viatge
 *   via [LaunchedEffect], crida [TripViewModel.updateTrip].
 *
 * La UI valida que tots els camps obligatoris tinguin contingut i que
 * la data de fi sigui posterior a la d'inici, mostrant errors en línia
 * abans d'habilitar el botó (validació capa UI).
 *
 * La validació de negoci final la fa el repositori a través del ViewModel,
 * i els errors pugen com a [errorMessage] mostrats amb un Toast.
 *
 * @param navController Controlador de navegació.
 * @param tripId Identificador del viatge a editar. Null si és una creació nova.
 * @param viewModel ViewModel gestionat per Hilt.
 */
@Composable
fun CreateTripScreen(
    navController: NavController,
    tripId: Int? = null,
    viewModel: TripViewModel = hiltViewModel()
) {
    val isEditMode = tripId != null
    val context = LocalContext.current
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

    // Mode edició: pre-omple el formulari amb les dades del viatge existent,
    LaunchedEffect(tripId) {
        if (tripId == null) return@LaunchedEffect
        val existing = viewModel.getTripById(tripId) ?: return@LaunchedEffect
        title         = existing.title
        description   = existing.description
        startDateText = sdf.format(existing.dateIn)
        endDateText   = sdf.format(existing.dateOut)
        startDate     = existing.dateIn
        endDate       = existing.dateOut
        Log.d(TAG, "Mode edició: pre-omplert viatge id=$tripId")
    }

    // Mostra els errors del domini/repositori com a Toast
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    val dateBeforeError = stringResource(R.string.date_before_error)

    // Validació en línia del rang de dates (capa UI)
    LaunchedEffect(startDate, endDate) {
        dateRangeError = if (startDate != null && endDate != null && !endDate!!.after(startDate)) {
            dateBeforeError
        } else null
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
                title = if (isEditMode) stringResource(R.string.edit_trip)
                else stringResource(R.string.new_trip),
                onBackClick = { navController.popBackStack() }
            )
        }
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
                    // En edició no es bloquegen dates passades (el viatge pot ja haver passat)
                    blockPastDates = !isEditMode,
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
                    blockPastDates = !isEditMode,
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

            // ── Botó de crear / guardar ──────────────────────────────────────

            val destinationErrorText = stringResource(R.string.destination_not_empty_error)
            val descriptionErrorText = stringResource(R.string.description_not_empty_error)

            Button(
                onClick = {
                    // Marcar camps buits amb error (validació capa UI)
                    var hasError = false
                    if (title.isBlank()) {
                        titleError = destinationErrorText
                        hasError = true
                    }
                    if (description.isBlank()) {
                        descriptionError = descriptionErrorText
                        hasError = true
                    }
                    if (hasError) return@Button

                    val success = if (isEditMode) {
                        // Edició
                        viewModel.updateTrip(
                            tripId = tripId!!,
                            title = title.trim(),
                            description = description,
                            dateIn = startDate!!,
                            dateOut = endDate!!
                        ).also { Log.i(TAG, "updateTrip: destí=$title, id=$tripId") }
                    } else {
                        // Creació
                        viewModel.addTrip(
                            title = title.trim(),
                            description = description,
                            dateIn = startDate!!,
                            dateOut = endDate!!,
                            userId = 1 // TODO: substituir per l'ID de l'usuari autenticat
                        ).also { Log.i(TAG, "addTrip: destí=$title") }
                    }

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
                    text = if (isEditMode) stringResource(R.string.save)
                    else stringResource(R.string.create_trip),
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

@Preview(showBackground = true)
@Composable
fun EditTripScreenPreview() {
    CreateTripScreen(rememberNavController(), tripId = 1)
}