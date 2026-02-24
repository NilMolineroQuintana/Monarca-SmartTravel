package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.domain.ItineraryItem
import com.example.monarcasmarttravel.domain.PlanType
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.TripCard
import com.example.monarcasmarttravel.ui.WideOption
import java.util.Calendar

@Composable
fun ItineraryScreen(navController: NavController) {
    val calendar = Calendar.getInstance()
    calendar.set(2026, Calendar.MARCH, 23)
    val dateIn = calendar.time
    calendar.set(2026, Calendar.MARCH, 30)
    val dateOut = calendar.time

    calendar.set(2026, Calendar.MARCH, 23, 10, 30)
    val dataVol = calendar.time

    calendar.set(2026, Calendar.MARCH, 23, 15, 0)
    val checkInHotel = calendar.time

    calendar.set(2026, Calendar.MARCH, 23, 17, 0)
    val checkInParking = calendar.time

    calendar.set(2026, Calendar.MARCH, 30, 11, 0)
    val checkOutGeneral = calendar.time

    calendar.set(2026, Calendar.MARCH, 25, 20, 30)
    val dataRest = calendar.time

    calendar.set(2026, Calendar.MARCH, 26, 12, 0)
    val dataMirador = calendar.time

    val mockData = listOf(
        ItineraryItem(id = 1, type = PlanType.FLIGHT, company = "Ryanair", locationName = "Barcelona", checkInDate = dataVol, transportNumber = "FR1234"),
        ItineraryItem(id = 2, type = PlanType.HOTEL, locationName = "Hotel FAWLTY", checkInDate = checkInHotel, checkOutDate = checkOutGeneral, address = "Aquest text és una prova pera veure com es veu el camp de direcció"),
        ItineraryItem(id = 3, type = PlanType.PARKING, locationName = "Parking FAWLTY", checkInDate = checkInParking, checkOutDate = checkOutGeneral, address = "Aquest text és una prova pera veure com es veu el camp de direcció"),
        ItineraryItem(id = 4, type = PlanType.RESTAURANT, locationName = "Restaurant FAWLTY", checkInDate = dataRest, address = "Aquest text és una prova pera veure com es veu el camp de direcció"),
        ItineraryItem(id = 5, type = PlanType.LOCATION, locationName = "Mirador FAWLTY", checkInDate = dataMirador, address = "Aquest text és una prova pera veure com es veu el camp de direcció")
    )
    val groupedData = mockData
        .sortedBy { it.checkInDate }
        .groupBy { it.formatDateKey(it.checkInDate) }

    Scaffold(
        topBar = { MyTopBar(showPageTitle = false, onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        LazyColumn (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                TripCard(
                    place = "Kioto, Japón",
                    dateIn = dateIn,
                    dateOut = dateOut,
                    showNextTitle = false,
                    modifier = Modifier
                        .padding(horizontal = AppDimensions.PaddingMedium)
                )
                Spacer(modifier = Modifier.size(AppDimensions.PaddingLarge))
            }
            if (false) {
                item {
                    Text(
                        text = "No tens cap plan",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                    )
                }
                item {
                    TextButton (
                        onClick = { navController.navigate("plan") },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.size(width = 200.dp, height = 50.dp)
                    ) {
                        Text(
                            text = "Afegir un nou",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                groupedData.forEach { (fecha, itemsDelDia) ->
                    item {
                        Text(
                            text = fecha,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(itemsDelDia) { plan ->
                        ItineraryItemComponent(plan)
                    }
                }
            }
        }
    }
}

@Composable
fun PlanOptionsScreen(navController: NavController) {

    val PopularPlans = listOf(PlanType.FLIGHT, PlanType.BOAT, PlanType.TRAIN,)
    val MorePlans = listOf(PlanType.HOTEL, PlanType.RESTAURANT, PlanType.LOCATION, PlanType.PARKING)

    Scaffold(
        topBar = { MyTopBar("Afegir plan", onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            item { Text(
                text = "Els més populars",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            val mod = Modifier.padding(vertical = 4.dp)

            items(PopularPlans) { plan ->
                WideOption(plan.icon, stringResource(id = plan.titleRes), showIcon = false, modifier = mod, onClick = { navController.navigate("plan/${plan.route}") })
            }

            item {
                Text (
                    text = "Més",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            items(MorePlans) { plan ->
                WideOption(plan.icon, stringResource(id = plan.titleRes), showIcon = false, modifier = mod, onClick = { navController.navigate("plan/${plan.route}") })
            }
        }
    }
}

@Composable
fun ItineraryItemComponent(ico: ImageVector, enterTime: String, title: String, secondaryText: String, tertiaryText: String = "")
{
    Surface(
        modifier = Modifier.padding(horizontal = AppDimensions.PaddingMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(
                text = enterTime,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(25.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
            ) {
                Icon(
                    imageVector = ico,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
            Column() {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = secondaryText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = tertiaryText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ItineraryItemComponent(item: ItineraryItem) {
    when (item.type.route) {
        "flight","boat","train" -> ItineraryItemComponent(
            item.type.icon,
            item.getCheckInTime(),
            "Origen: ${item.locationName}",
            "${item.transportNumber} (${item.company})",
        )
        "hotel","parking","location","restaurant" -> ItineraryItemComponent(
            item.type.icon,
            item.getCheckInTime(),
            item.locationName,
            item.address
        )
    }
}

@Preview
@Composable
fun ItineraryItemPreview() {
    ItineraryItemComponent(Icons.Filled.Hotel,"Sakura Mori Retreat", "23/03/2026 18:55", "3-chōme-43-15 Sendagi, Bunkyo City, Tokyo 113-0022, Japón")
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItineraryPreview() {
    ItineraryScreen(rememberNavController())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlanScreenPreview() {
    PlanOptionsScreen(rememberNavController())
}
