package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.Trip
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.TripCard
import java.util.Calendar

@Composable
fun TripsScreen(navController: NavController) {
    val labelKyoto = stringResource(R.string.kyoto)
    val labelParis = stringResource(R.string.paris)
    val labelNY = stringResource(R.string.NY)

    // Viatges Mockup
    val mockTrips = remember {
        val calendar = Calendar.getInstance()

        // Viatge 1: Japó
        calendar.set(2026, Calendar.MARCH, 23)
        val in1 = calendar.time
        calendar.set(2026, Calendar.MARCH, 30)
        val out1 = calendar.time

        // Viatge 2: França
        calendar.set(2026, Calendar.MAY, 15)
        val in2 = calendar.time
        calendar.set(2026, Calendar.MAY, 22)
        val out2 = calendar.time

        // Viatge 3: EUA
        calendar.set(2026, Calendar.AUGUST, 10)
        val in3 = calendar.time
        calendar.set(2026, Calendar.AUGUST, 25)
        val out3 = calendar.time

        listOf(
            Trip(1, labelKyoto, in1, out1, R.drawable.kyoto),
            Trip(2, labelParis, in2, out2, R.drawable.paris),
            Trip(3, labelNY, in3, out3, R.drawable.newyork),
        )
    }

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.trips_text)) },
        bottomBar = { MyBottomBar(navController) },

        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("createTrip") }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
                contentPadding = PaddingValues(
                    start = AppDimensions.PaddingMedium,
                    end = AppDimensions.PaddingMedium,
                    bottom = AppDimensions.PaddingLarge
                )
            ) {
                itemsIndexed(mockTrips) { index, trip ->
                    TripCard(
                        trip = trip,
                        onClick = { navController.navigate("itinerary/${trip.id}") }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    TripsScreen(rememberNavController())
}