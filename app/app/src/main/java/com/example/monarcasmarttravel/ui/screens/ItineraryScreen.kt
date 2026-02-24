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
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, 2026)
                    set(Calendar.MONTH, Calendar.FEBRUARY)
                    set(Calendar.DAY_OF_MONTH, 15)
                    set(Calendar.HOUR_OF_DAY, 14)
                    set(Calendar.MINUTE, 30)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val mockData = listOf(
                    ItineraryItem(id = 1, type = PlanType.TRAIN, company = "Ryanair", locationName = "Barcelona", checkInDate = calendar.time, transportNumber = "FR1234"),
                    ItineraryItem(id = 2, type = PlanType.HOTEL, locationName = "Hotel FAWLTY", checkInDate = calendar.time, checkOutDate = dateOut, address = "Aquest text és una prova pera veure com es veu el camp de direcció"),
                    ItineraryItem(id = 3, type = PlanType.PARKING, locationName = "Parking FAWLTY", checkInDate = calendar.time, checkOutDate = dateOut, address = "Aquest text és una prova pera veure com es veu el camp de direcció"),
                    ItineraryItem(id = 4, type = PlanType.RESTAURANT, locationName = "Restaurant FAWLTY", checkInDate = calendar.time, address = "Aquest text és una prova pera veure com es veu el camp de direcció"),
                    ItineraryItem(id = 5, type = PlanType.LOCATION, locationName = "Mirador FAWLTY", checkInDate = calendar.time, address = "Aquest text és una prova pera veure com es veu el camp de direcció")
                )
                items (mockData) { item ->
                    ItineraryItemComponent(item)
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
