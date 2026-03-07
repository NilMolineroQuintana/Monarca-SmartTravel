package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.AppTextField
import com.example.monarcasmarttravel.ui.MyTopBar
import java.text.SimpleDateFormat
import java.util.Calendar
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
 * Els estats del formulari es preserven en rotació de pantalla mitjançant [rememberSaveable].
 * En confirmar, navega de tornada a la pantalla anterior.
 *
 * @param navController Controlador de navegació.
 * @param ruta Tipus de pla a afegir. Valors possibles: "flight", "boat", "train",
 *             "hotel", "restaurant", "location".
 */
@Composable
fun PlanScreen(navController: NavController, ruta: String?) {

    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

    // Estats dels camps del formulari, persistents en rotació de pantalla
    var locationName by rememberSaveable { mutableStateOf("") }
    var destination by rememberSaveable { mutableStateOf("") }
    var checkInDate by rememberSaveable { mutableStateOf(date) }
    var checkOutDate by rememberSaveable { mutableStateOf(date) }
    var address by rememberSaveable { mutableStateOf("") }
    var company by rememberSaveable { mutableStateOf("") }
    var transportNumber by rememberSaveable { mutableStateOf("") }

    // Tipus que es consideren transport (no mostren camp de nom ni adreça)
    val transports = listOf("train", "boat", "flight")

    // Tipus pels quals no té sentit mostrar la data de sortida
    val excludeCheckOut = listOf("train", "boat", "flight", "location", "restaurant")

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
        else -> "Identificador"
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            // Camps exclusius per a plans que no són transport
            if (ruta !in transports) {
                item {
                    AppTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = labelName,
                        placeholder = labelName,
                        leadingIcon = Icons.Default.Place,
                    )
                }
                item {
                    AppTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = stringResource(R.string.direccio),
                        placeholder = stringResource(R.string.direccio),
                        leadingIcon = Icons.Default.Home,
                    )
                }
            }

            // Camps exclusius per a plans de transport
            if (ruta in transports) {
                item {
                    AppTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = stringResource(R.string.origin),
                        placeholder = stringResource(R.string.origin),
                        leadingIcon = Icons.Default.LocationOn,
                    )
                }
                item {
                    AppTextField(
                        value = destination,
                        onValueChange = { destination = it },
                        label = stringResource(R.string.destination),
                        placeholder = stringResource(R.string.destination),
                        leadingIcon = Icons.Default.Flag,
                    )
                }
                item {
                    AppTextField(
                        value = company,
                        onValueChange = { company = it },
                        label = stringResource(R.string.company),
                        placeholder = stringResource(R.string.company),
                        leadingIcon = Icons.Default.Business,
                    )
                }
                item {
                    AppTextField(
                        value = transportNumber,
                        onValueChange = { transportNumber = it },
                        label = transportName,
                        placeholder = transportName,
                        leadingIcon = Icons.Default.ConfirmationNumber,
                    )
                }
            }

            // Camp(s) de data: sempre es mostra entrada; sortida s'oculta per a certs tipus
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppTextField(
                        value = checkInDate,
                        onValueChange = { checkInDate = it },
                        label = stringResource(R.string.data_de_entrada),
                        placeholder = "dd/MM/yyyy",
                        leadingIcon = Icons.Default.CalendarToday,
                        modifier = Modifier.weight(1f)
                    )
                    if (ruta !in excludeCheckOut) {
                        AppTextField(
                            value = checkOutDate,
                            onValueChange = { checkOutDate = it },
                            label = stringResource(R.string.data_de_sortida),
                            placeholder = "dd/MM/yyyy",
                            leadingIcon = Icons.Default.CalendarToday,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Botó per confirmar i afegir el pla; torna a la pantalla anterior
            item {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    shape = RoundedCornerShape(20.dp),
                    onClick = { navController.popBackStack() },
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
    PlanScreen(rememberNavController(), "hotel")
}

@Preview(showBackground = true)
@Composable
fun RestaurantScreenPreview() {
    PlanScreen(rememberNavController(), "flight")
}