package com.example.monarcasmarttravel.ui.screens.trip

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.WideOption
import com.example.monarcasmarttravel.ui.WideOptionAction

/**
 * Pantalla de selecció del tipus de pla a afegir a l'itinerari.
 *
 * Mostra dues seccions: els plans més habituals (transport) i altres opcions
 * (allotjament, restaurant, lloc). En seleccionar un tipus, navega a [PlanScreen]
 * amb la ruta corresponent.
 *
 * @param navController Controlador de navegació.
 */
@Composable
fun PlanOptionsScreen(navController: NavController, tripId: Int) {
    LaunchedEffect(Unit) {
        Log.d("PlanOptionsScreen", "tripId: $tripId")
    }

    // Plans de transport, considerats els més freqüents
    val popularPlans = listOf(PlanType.FLIGHT, PlanType.BOAT, PlanType.TRAIN)

    // Resta de tipus de plans disponibles
    val morePlans = listOf(PlanType.HOTEL, PlanType.RESTAURANT, PlanType.LOCATION)

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.add_plan), onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            // Secció de plans populars
            item {
                Text(
                    text = stringResource(R.string.most_popular),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            val mod = Modifier.padding(vertical = 4.dp)

            items(popularPlans) { plan ->
                // Cada opció navega a la pantalla de formulari del tipus de pla corresponent
                WideOption(
                    ico = plan.icon,
                    text = stringResource(id = plan.titleRes),
                    action = WideOptionAction.None,
                    modifier = mod,
                    onClick = { navController.navigate("plan/${plan.route}/$tripId") }
                )
            }

            // Secció amb la resta d'opcions de pla
            item {
                Text(
                    text = stringResource(R.string.more),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            items(morePlans) { plan ->
                WideOption(
                    ico = plan.icon,
                    text = stringResource(id = plan.titleRes),
                    action = WideOptionAction.None,
                    modifier = mod,
                    onClick = { navController.navigate("plan/${plan.route}/$tripId") }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlanScreenPreview() {
    PlanOptionsScreen(rememberNavController(), 1)
}