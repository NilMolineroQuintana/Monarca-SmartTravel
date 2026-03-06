package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import com.example.monarcasmarttravel.ui.MyTopBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Pantalla de formulari per afegir un nou pla a l'itinerari.
 *
 * Els camps mostrats s'adapten dinàmicament segons el tipus de pla ([ruta]):
 * - Transport (flight, boat, train): origen, destí, companyia, número i data de sortida.
 * - Allotjament/POI (hotel, restaurant, location): nom, adreça i dates d'entrada/sortida.
 *
 * @param navController Controlador de navegació.
 * @param ruta Tipus de pla seleccionat (p. ex. "flight", "hotel", "restaurant"...).
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
                    TextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = { Text(labelName) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text(stringResource(R.string.direccio)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Camps exclusius per a plans de transport
            if (ruta in transports) {
                item {
                    TextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = { Text(stringResource(R.string.origin)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    TextField(
                        value = destination,
                        onValueChange = { destination = it },
                        label = { Text(stringResource(R.string.destination)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    TextField(
                        value = company,
                        onValueChange = { company = it },
                        label = { Text(stringResource(R.string.company)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    TextField(
                        value = transportNumber,
                        onValueChange = { transportNumber = it },
                        label = { Text(transportName) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Camp(s) de data: sempre es mostra entrada; sortida s'oculta per a certs tipus
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = checkInDate,
                        onValueChange = { checkInDate = it },
                        label = { Text(stringResource(R.string.data_de_entrada)) },
                        modifier = Modifier.weight(1f)
                    )
                    if (ruta !in excludeCheckOut) {
                        TextField(
                            value = checkOutDate,
                            onValueChange = { checkOutDate = it },
                            label = { Text(stringResource(R.string.data_de_sortida)) },
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