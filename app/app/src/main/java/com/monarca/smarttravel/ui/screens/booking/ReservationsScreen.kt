package com.monarca.smarttravel.ui.screens.booking

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.monarca.smarttravel.R
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.MyBottomBar
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.PopUp
import com.monarca.smarttravel.ui.viewmodels.TripViewModel
import com.monarca.smarttravel.utils.Constants
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReservationsScreen(navController: NavController) {
    val viewModel: TripViewModel = hiltViewModel()
    val reservations by viewModel.reservations.collectAsStateWithLifecycle()
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    PopUp(
        show = tripToDelete != null,
        title = stringResource(R.string.cancel_reservation),
        text = stringResource(R.string.cancel_reservation_message),
        acceptText = stringResource(R.string.delete),
        onAccept = {
            tripToDelete?.let { viewModel.deleteTrip(it.id) }
            tripToDelete = null
        },
        onDismiss = { tripToDelete = null }
    )

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.my_reservations), onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        if (reservations.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(AppDimensions.PaddingLarge),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🏨", style = MaterialTheme.typography.displayMedium)
                Text(
                    text = stringResource(R.string.no_reservations),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = AppDimensions.PaddingMedium)
                )
                Text(
                    text = stringResource(R.string.no_reservations_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = AppDimensions.PaddingSmall)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
                contentPadding = PaddingValues(
                    horizontal = AppDimensions.PaddingMedium,
                    vertical = AppDimensions.PaddingSmall
                )
            ) {
                items(reservations, key = { it.id }) { trip ->
                    ReservationCard(trip, onDelete = { tripToDelete = trip })
                }
            }
        }
    }
}

@Composable
fun ReservationCard(trip: Trip, onDelete: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(110.dp)
        ) {
            // Imatge de l'hotel
            AsyncImage(
                model = trip.hotelImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            // Informació de la reserva
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(AppDimensions.PaddingMedium)
            ) {
                Text(
                    text = trip.hotelName ?: trip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = Constants.marqueeRepeat,
                        initialDelayMillis = Constants.marqueeDelay
                    )
                )
                trip.roomType?.let {
                    Text(
                        text = it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "${dateFormat.format(trip.dateIn)}",
                    style = MaterialTheme.typography.bodySmall
                )
                trip.reservationId?.let {
                    Text(
                        text = stringResource(R.string.id_label, it),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // Botó d'eliminar
            IconButton(onClick = onDelete, modifier = Modifier.padding(end = 4.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}