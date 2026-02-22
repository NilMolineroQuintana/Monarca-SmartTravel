package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
                onClick = {},
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

@Composable
fun PlanScreen(navController: NavController) {
    Scaffold(
        topBar = { MyTopBar("Afegir plan", onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Els més populars",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp, top = 20.dp)
            )
            val mod = Modifier.padding(vertical = 4.dp)

            WideOption(Icons.Filled.FlightTakeoff, "Vol", showIcon = false, modifier = mod)
            WideOption(Icons.Filled.DirectionsBoatFilled, "Vaixell", showIcon = false, modifier = mod)
            WideOption(Icons.Filled.Train, "Tren", showIcon = false, modifier = mod)

            Text(
                text = "Més",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            WideOption(Icons.Filled.Hotel, "Allotjament", showIcon = false, modifier = mod)
            WideOption(Icons.Filled.Restaurant, "Restaurant", showIcon = false, modifier = mod)
            WideOption(Icons.Filled.LocationCity, "Lloc d'interès", showIcon = false, modifier = mod)
            WideOption(Icons.Filled.DirectionsBoatFilled, "Restaurant", showIcon = false, modifier = mod)
            WideOption(Icons.Filled.LocalParking, "Aparcament", showIcon = false, modifier = mod)
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