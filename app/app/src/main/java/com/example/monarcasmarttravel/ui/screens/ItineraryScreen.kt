package com.example.monarcasmarttravel.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBoatFilled
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.monarcasmarttravel.R
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding)
        ) {
            TripCard(
                place = "Kioto, Japón",
                dateIn = dateIn,
                dateOut = dateOut,
                showNextTitle = false
            )
            Text(
                text = "No tens cap plan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
            )
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
    }
}

enum class PlanType(val titleRes: Int, val icon: ImageVector, val route: String) {
    FLIGHT(R.string.plan_flight, Icons.Default.FlightTakeoff, "detail_flight"),
    BOAT(R.string.plan_boat, Icons.Default.DirectionsBoatFilled, "detail_boat"),
    TRAIN(R.string.plan_train, Icons.Default.Train, "detail_train"),
    HOTEL(R.string.plan_hotel, Icons.Default.Hotel, "detail_hotel"),
    RESTAURANT(R.string.plan_restaurant, Icons.Default.Restaurant, "detail_restaurant"),
    LOCATION(R.string.plan_location, Icons.Default.LocationCity, "detail_location"),
    PARKING(R.string.plan_parking, Icons.Default.LocalParking, "detail_parking")
}

@Composable
fun PlanScreen(navController: NavController) {

    val PopularPlans = listOf(PlanType.FLIGHT, PlanType.BOAT, PlanType.TRAIN,)
    val MorePlans = listOf(PlanType.HOTEL, PlanType.RESTAURANT, PlanType.LOCATION, PlanType.PARKING)

    Scaffold(
        topBar = { MyTopBar("Afegir plan", onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item { Text(
                text = "Els més populars",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp, top = 20.dp)
                )
            }

            val mod = Modifier.padding(vertical = 4.dp)

            items(PopularPlans) { plan ->
                WideOption(plan.icon, stringResource(id = plan.titleRes), showIcon = false, modifier = mod, onClick = {})
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
                WideOption(plan.icon, stringResource(id = plan.titleRes), showIcon = false, modifier = mod, onClick = {})
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItineraryPreview() {
    ItineraryScreen(rememberNavController())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlanScreenPreview() {
    PlanScreen(rememberNavController())
}
