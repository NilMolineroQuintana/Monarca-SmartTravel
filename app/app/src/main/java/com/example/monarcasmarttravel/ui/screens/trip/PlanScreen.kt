package com.example.monarcasmarttravel.ui.screens.trip

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.AppTextField
import com.example.monarcasmarttravel.ui.DateField
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodels.ItineraryItemViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Pantalla de formulari per afegir un nou pla a l'itinerari.
 *
 * Els camps del formulari s'adapten dinàmicament segons el tipus de pla indicat per [ruta]:
 * - **Transport** (flight, boat, train): mostra origen, destí, companyia, número de transport
 *   i data de sortida.
 * - **Allotjament / Punt d'interès** (hotel, restaurant, location): mostra nom, adreça,
 *   data d'entrada i, en el cas d'hotels, data de sortida.
 *
 * Els camps de data utilitzen [DateField], que obre un DatePicker natiu de Material3
 * en ser premuts i no permeten entrada manual de text.
 *
 * Els estats del formulari es preserven en rotació de pantalla mitjançant [rememberSaveable].
 * En confirmar, navega de tornada a la pantalla anterior.
 *
 * @param navController Controlador de navegació.
 * @param ruta Tipus de pla a afegir. Valors possibles: "flight", "boat", "train",
 *             "hotel", "restaurant", "location".
 */
@Composable
fun PlanScreen(navController: NavController, ruta: String?, tripId: Int) {
    Log.d("PlanScreen", "route: $ruta, tripId: $tripId")
    val viewModel: ItineraryItemViewModel = hiltViewModel()

    // Estats dels camps del formulari, persistents en rotació de pantalla
    var locationName by rememberSaveable { mutableStateOf("") }
    var destination by rememberSaveable { mutableStateOf("") }
    var checkInDate by rememberSaveable { mutableStateOf("") }
    // var checkOutDate by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var company by rememberSaveable { mutableStateOf("") }
    var transportNumber by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }

    // Tipus que es consideren transport (no mostren camp de nom ni adreça)
    val transports = listOf("train", "boat", "flight")

    // Tipus pels quals no té sentit mostrar la data de sortida
    // val excludeCheckOut = listOf("train", "boat", "flight", "location", "restaurant")

    // Títol de la pantalla segons el tipus de pla
    val titleName = when (ruta) {
        "flight" -> stringResource(R.string.plan_flight)
        "boat" -> stringResource(R.string.plan_boat)
        "train" -> stringResource(R.string.plan_train)
        "hotel" -> stringResource(R.string.plan_hotel)
        "restaurant" -> stringResource(R.string.plan_restaurant)
        "location" -> stringResource(R.string.plan_location)
        else -> "Nom"
    }

    // Etiqueta del camp de nom, adaptada al tipus de pla
    val labelName = when (ruta) {
        "hotel" -> stringResource(R.string.hotel_name)
        "restaurant" -> stringResource(R.string.restaurant_name)
        "location" -> stringResource(R.string.location_name)
        else -> "Nom"
    }

    // Etiqueta del camp d'identificador del transport (número de vol, tren, etc.)
    val transportName = when (ruta) {
        "flight" -> stringResource(R.string.flight_num)
        "boat" -> stringResource(R.string.boat_num)
        "train" -> stringResource(R.string.train_num)
        else -> ""
    }

    Scaffold(
        topBar = {
            MyTopBar(
                "${stringResource(R.string.add)} ${titleName.lowercase()}",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxSize()
        ) {

            // Camp de nom (només per a plans no-transport)
            if (ruta !in transports) {
                item {
                    AppTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = labelName,
                        placeholder = labelName,
                        leadingIcon = Icons.Default.Home
                    )
                }
                item {
                    AppTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = stringResource(R.string.direccio),
                        placeholder = stringResource(R.string.direccio),
                        leadingIcon = Icons.Default.Place
                    )
                }
            }

            // Camps exclusius per a plans de transport
            if (ruta in transports) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTextField(
                            value = locationName,
                            onValueChange = { locationName = it },
                            label = stringResource(R.string.origin),
                            placeholder = stringResource(R.string.origin),
                            leadingIcon = Icons.Default.LocationOn,
                            modifier = Modifier.weight(1f)
                        )
                        AppTextField(
                            value = destination,
                            onValueChange = { destination = it },
                            label = stringResource(R.string.destination),
                            placeholder = stringResource(R.string.destination),
                            leadingIcon = Icons.Default.Flag,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTextField(
                            value = company,
                            onValueChange = { company = it },
                            label = stringResource(R.string.company),
                            placeholder = stringResource(R.string.company),
                            leadingIcon = Icons.Default.Business,
                            modifier = Modifier.weight(1f)
                        )
                        AppTextField(
                            value = transportNumber,
                            onValueChange = { transportNumber = it },
                            label = transportName,
                            placeholder = transportName,
                            leadingIcon = Icons.Default.ConfirmationNumber,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Camp(s) de data: sempre es mostra entrada; sortida s'oculta per a certs tipus.
            // S'utilitza DateField, que obre un DatePickerPopUp i no permet entrada manual.
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DateField(
                        value = checkInDate,
                        onDateSelected = { checkInDate = it },
                        label = stringResource(R.string.data_de_entrada),
                        modifier = Modifier.weight(1f)
                    )
                    /*
                    if (ruta !in excludeCheckOut) {
                        DateField(
                            value = checkOutDate,
                            onDateSelected = { checkOutDate = it },
                            label = stringResource(R.string.data_de_sortida),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    */
                }
            }

            // Camp per al preu
            item {
                AppTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = stringResource(R.string.price),
                    placeholder = "Preu",
                    leadingIcon = Icons.Default.AttachMoney
                )
            }

            // Botó per confirmar i afegir el pla; torna a la pantalla anterior
            item {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        val planType = PlanType.entries.find { it.route == ruta } ?: return@TextButton

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                        val defaultDate = dateTimeFormat.parse("23/03/2026 10:00")

                        val parsedDate = if (checkInDate.isNotEmpty()) {
                            runCatching { dateTimeFormat.parse("$checkInDate 10:00") }.getOrNull() ?: defaultDate
                        } else {
                            defaultDate
                        }

                        val newItem = if (ruta in transports) {
                            ItineraryItem(
                                id = 0,
                                tripId = tripId,
                                type = planType,
                                price = price.toDoubleOrNull() ?: 0.0,
                                origin = locationName,
                                destination = destination,
                                company = company,
                                transportNumber = transportNumber,
                                departureDate = parsedDate
                            )
                        } else {
                            ItineraryItem(
                                id = 0,
                                tripId = tripId,
                                type = planType,
                                price = price.toDoubleOrNull() ?: 0.0,
                                locationName = locationName,
                                address = address,
                                checkInDate = parsedDate
                            )
                        }

                        viewModel.addItem(newItem)

                        navController.navigate("itinerary/$tripId") {
                            popUpTo("itinerary/$tripId") { inclusive = true }
                        }
                    },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(stringResource(R.string.add))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotelScreenPreview() {
    PlanScreen(rememberNavController(), "hotel", 1)
}

@Preview(showBackground = true)
@Composable
fun RestaurantScreenPreview() {
    PlanScreen(rememberNavController(), "flight", 1)
}