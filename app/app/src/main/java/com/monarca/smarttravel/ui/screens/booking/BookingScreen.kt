package com.monarca.smarttravel.ui.screens.booking

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.monarca.smarttravel.R
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.DateField
import com.monarca.smarttravel.ui.MyBottomBar
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.viewmodels.BookingUiState
import com.monarca.smarttravel.ui.viewmodels.BookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val cities = mapOf(
    "Paris" to "PAR",
    "Barcelona" to "BCN",
    "London" to "LON"
)

private const val DATE_FORMAT = "dd/MM/yyyy"

@Composable
fun BookingScreen(navController: NavController) {

    val viewModel: BookingViewModel = hiltViewModel()

    var selected by remember { mutableStateOf("") }
    var startDateText by remember() { mutableStateOf("") }
    var endDateText by remember() { mutableStateOf("") }
    var startDate     by remember() { mutableStateOf<Date?>(null) }
    var endDate       by remember() { mutableStateOf<Date?>(null) }

    val sdf = remember { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }
    var dateRangeError by remember { mutableStateOf<String?>(null) }

    val errorDateRange = stringResource(R.string.error_date_range)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        when (uiState) {
            is BookingUiState.Success -> {
                navController.navigate("bookList")
                viewModel.resetState()
            }
            is BookingUiState.Error -> {
                Log.e("BookingScreen", "Error: ${(uiState as BookingUiState.Error).error.name}")
                viewModel.resetState()
            }
            else -> {}
        }
    }

    LaunchedEffect(startDate, endDate) {
        dateRangeError = if (startDate != null && endDate != null && !endDate!!.after(startDate)) {
            errorDateRange
        } else null
    }

    val isFormValid = startDate != null
            && endDate != null
            && dateRangeError == null
            && selected.isNotBlank()

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.book)) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.where_book),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = AppDimensions.PaddingSmall)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimensions.PaddingMedium)
            ) {
                cities.keys.forEach { city ->
                    CityCard(
                        city = city,
                        modifier = Modifier.weight(1f),
                        selected = city == selected,
                        onClick = { selected = city }
                    )
                }
            }
            Text(
                text = stringResource(R.string.when_book),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = AppDimensions.PaddingSmall)
            )
            Column (
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
                modifier = Modifier.padding(bottom = AppDimensions.PaddingMedium)
            ) {
                DateField(
                    value = startDateText,
                    label = stringResource(R.string.start_date),
                    modifier = Modifier.fillMaxWidth(),
                    onDateSelected = { dateStr ->
                        startDateText = dateStr
                        startDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                    },
                    blockPastDates = true,
                    showTime = false
                )
                DateField(
                    value = endDateText,
                    label = stringResource(R.string.final_date),
                    modifier = Modifier.fillMaxWidth(),
                    onDateSelected = { dateStr ->
                        endDateText = dateStr
                        endDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                    },
                    blockPastDates = true,
                    showTime = false
                )
                // Missatge d'error en línia per al rang de dates (validació capa UI)
                if (dateRangeError != null) {
                    Text(
                        text = dateRangeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = isFormValid,
                onClick = {
                    val cityCode = cities[selected] ?: ""

                    val apiStartDate = startDateText.split("/").reversed().joinToString("-")
                    val apiEndDate = endDateText.split("/").reversed().joinToString("-")

                    viewModel.getAvailable(apiStartDate, apiEndDate, cityCode, cityName = selected)
                }
            ) {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(
                onClick = { navController.navigate("bookReservations") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Assignment, contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.my_reservations))
            }
            Spacer(modifier = Modifier.size(innerPadding.calculateBottomPadding()))
        }
    }
}

@Composable
fun CityCard(
    city: String,
    selected: Boolean = false,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else Color.Transparent

    val imageId = when(city) {
        "Paris" -> R.drawable.paris
        "Barcelona" -> R.drawable.barcelona
        "London" -> R.drawable.london
        else -> R.drawable.ic_launcher_foreground
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier.aspectRatio(0.78f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
            )
            Text(
                text = city,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview(){
    BookingScreen(rememberNavController())
}

@Preview
@Composable
fun CityCardPreview() {
    CityCard("London")
}