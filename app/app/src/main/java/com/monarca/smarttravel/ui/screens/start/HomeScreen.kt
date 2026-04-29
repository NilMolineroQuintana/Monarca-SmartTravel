package com.monarca.smarttravel.ui.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.monarca.smarttravel.R
import com.monarca.smarttravel.ui.MyBottomBar
import com.monarca.smarttravel.ui.TripCard
import com.monarca.smarttravel.ui.WideOption
import com.monarca.smarttravel.ui.viewmodels.TripViewModel

/**
 * Pantalla principal de l'aplicació.
 *
 * Mostra el viatge futur més proper obtingut del [TripViewModel].
 * Si no hi ha cap viatge pròxim, la secció de la targeta no es renderitza.
 *
 * La [TripCard] suporta clic llarg per editar o eliminar el viatge desde
 * qualsevol lloc.
 *
 * @param navController Controlador de navegació.
 * @param viewModel ViewModel gestionat per Hilt.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TripViewModel = hiltViewModel()
) {
    // Viatge futur més proper
    val nextTrip by viewModel.nextTrip.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            HeaderSection()

            // Mostra la targeta del pròxim viatge només si existeix.
            if (nextTrip != null) {
                TripCard(
                    trip = nextTrip!!,
                    navController = navController,
                    onDeleted = { viewModel.deleteTrip(nextTrip!!.id) }
                )
            }

            QuickActionsSection(navController)
        }
    }
}

/**
 * Secció de capçalera de la pantalla principal.
 *
 * Mostra el logotip de l'aplicació, el nom de l'app i un subtítol de benvinguda.
 * Dissenyada per ocupar la part superior de [HomeScreen].
 */
@Composable
fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.logo_monarca),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(6f)
                    .padding(start = 10.dp, top = 15.dp)
            )
        }

        Text(
            text = stringResource(R.string.which_adventure),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Secció d'accions ràpides de la pantalla principal.
 *
 * Mostra un títol i una llista d'opcions que permeten a l'usuari accedir ràpidament
 * a les funcionalitats principals de l'app, com ara crear un nou viatge.
 *
 * @param navController Controlador de navegació per gestionar les accions de cada opció.
 */
@Composable
fun QuickActionsSection(navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.quick_actions),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )

        WideOption(
            ico = Icons.Filled.Map,
            text = stringResource(R.string.create_new_trip),
            secondaryText = stringResource(R.string.create_new_trip_description),
            onClick = {
                navController.navigate("createTrip")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}