package com.monarca.smarttravel.ui.screens.booking

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.viewmodels.AuthViewModel
import com.monarca.smarttravel.ui.viewmodels.BookingUiState
import com.monarca.smarttravel.ui.viewmodels.BookingViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@SuppressLint("DefaultLocale")
@Composable
fun BookingConfirmationScreen(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry("book")
    }
    val viewModel: BookingViewModel = hiltViewModel(parentEntry)
    val authViewModel: AuthViewModel = hiltViewModel()
    
    val hotel by viewModel.selectedHotel.collectAsStateWithLifecycle()
    val room by viewModel.selectedRoom.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user by authViewModel.user.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val nights = remember(startDate, endDate) {
        try {
            val start = LocalDate.parse(startDate)
            val end = LocalDate.parse(endDate)
            ChronoUnit.DAYS.between(start, end)
        } catch (e: Exception) {
            1L
        }
    }

    val totalPrice = (room?.price ?: 0.0) * nights

    LaunchedEffect(uiState) {
        when (uiState) {
            is BookingUiState.Success -> {
                navController.navigate("home") {
                    popUpTo("book") { inclusive = true }
                }
                viewModel.resetState()
            }
            is BookingUiState.Error -> {
                snackbarHostState.showSnackbar("Error al confirmar la reserva. Intenta-ho de nou.")
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { MyTopBar(title = "Resum de la reserva", onBackClick = { navController.popBackStack() }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(AppDimensions.PaddingMedium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(AppDimensions.PaddingMedium)) {
                    Text(text = hotel?.name ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(text = hotel?.address ?: "", style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = AppDimensions.PaddingSmall),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                    Text(text = "Habitació: ${room?.room_type ?: ""}", fontWeight = FontWeight.SemiBold)
                    Text(text = "Dates: $startDate a $endDate")
                    Text(text = "Estada: $nights nits")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Preu Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = "${String.format("%.2f", totalPrice)}€",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = { 
                    user?.let {
                        viewModel.bookRoom(
                            guestName = it.username,
                            email = it.email
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is BookingUiState.Loading && user != null
            ) {
                if (uiState is BookingUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Confirmar Reserva", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
