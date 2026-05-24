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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.monarca.smarttravel.R
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.viewmodels.AuthViewModel
import com.monarca.smarttravel.ui.viewmodels.BookingUiState
import com.monarca.smarttravel.ui.viewmodels.BookingViewModel
import com.monarca.smarttravel.ui.viewmodels.TripViewModel
import com.monarca.smarttravel.utils.Constants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun BookingConfirmationScreen(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry("book")
    }
    val viewModel: BookingViewModel = hiltViewModel(parentEntry)
    val authViewModel: AuthViewModel = hiltViewModel()
    val tripViewModel: TripViewModel = hiltViewModel()
    val lastBookingResponse by viewModel.lastBookingResponse.collectAsStateWithLifecycle()
    
    val hotel by viewModel.selectedHotel.collectAsStateWithLifecycle()
    val room by viewModel.selectedRoom.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
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

    val errorBooking = stringResource(R.string.booking_error)
    val defaultHotelName = stringResource(R.string.hotel_default_name)
    val defaultRoomType = stringResource(R.string.room_default_type)

    LaunchedEffect(uiState) {
        when (uiState) {
            is BookingUiState.Success -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateIn = runCatching { sdf.parse(startDate) }.getOrNull() ?: Date()
                val dateOut = runCatching { sdf.parse(endDate) }.getOrNull() ?: Date()

                tripViewModel.addTripFromBooking(
                    hotelName = hotel?.name ?: defaultHotelName,
                    hotelAddress = hotel?.address ?: "",
                    roomType = room?.room_type ?: defaultRoomType,
                    dateIn = dateIn,
                    dateOut = dateOut,
                    reservationId = lastBookingResponse?.reservation?.id ?: "",
                    hotelImageUrl = hotel?.image_url?.let { Constants.BASE_URL + it },
                    totalPrice = totalPrice,
                    cityName = selectedCity
                )
                navController.navigate("home") {
                    popUpTo("book") { inclusive = true }
                }
                viewModel.resetState()
            }
            is BookingUiState.Error -> {
                snackbarHostState.showSnackbar(errorBooking)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { MyTopBar(title = stringResource(R.string.booking_summary), onBackClick = { navController.popBackStack() }) },
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
                    Text(text = stringResource(R.string.room_label, room?.room_type ?: ""), fontWeight = FontWeight.SemiBold)
                    Text(text = stringResource(R.string.dates_range, startDate, endDate))
                    Text(text = stringResource(R.string.stay_nights, nights))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.total_price), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
                    Text(text = stringResource(R.string.confirm_booking), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
