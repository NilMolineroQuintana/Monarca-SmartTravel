package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsBoatFilled
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.ItineraryItem
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.PopUp
import com.example.monarcasmarttravel.ui.TopBarAction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class PlanType(
    val titleRes: Int,
    val icon: ImageVector,
    val route: String,
    val backgroundColor: Color,
    val iconColor: Color
) {
    FLIGHT(
        R.string.plan_flight,
        Icons.Default.FlightTakeoff,
        "flight",
        Color(0xFFB3E5FC),
        Color(0xFF0277BD)
    ),
    BOAT(
        R.string.plan_boat,
        Icons.Default.DirectionsBoatFilled,
        "boat",
        Color(0xFFB2DFDB),
        Color(0xFF00695C)
    ),
    TRAIN(
        R.string.plan_train,
        Icons.Default.Train,
        "train",
        Color(0xFFE1BEE7),
        Color(0xFF6A1B9A)
    ),
    HOTEL(
        R.string.plan_hotel,
        Icons.Default.Hotel,
        "hotel",
        Color(0xFFFFCCBC),
        Color(0xFFD84315)
    ),
    RESTAURANT(
        R.string.plan_restaurant,
        Icons.Default.Restaurant,
        "restaurant",
        Color(0xFFC8E6C9),
        Color(0xFF2E7D32)
    ),
    LOCATION(
        R.string.plan_location,
        Icons.Default.LocationCity,
        "location",
        Color(0xFFFFF9C4),
        Color(0xFFF9A825)
    )
}


@Composable
fun ItineraryScreen(navController: NavController, tripId: Int) {
    val calendar = Calendar.getInstance()

    // 1. Lògica de selecció de dades (Maquetat)
    val (destinationName, headerImg, dateIn, dateOut, mockData) = remember(tripId) {
        when (tripId) {
            2 -> { // PARÍS
                val dIn = calendar.apply { set(2026, Calendar.MAY, 15, 8, 30) }.time
                val dOut = calendar.apply { set(2026, Calendar.MAY, 22, 18, 0) }.time

                val items = listOf(
                    ItineraryItem(
                        id = 1, tripId = tripId, type = PlanType.FLIGHT,
                        company = "Air France", transportNumber = "AF1024",
                        origin = "Barcelona", destination = "Aeroport Charles de Gaulle",
                        departureDate = dIn,
                        price = 180.0
                    ),
                    ItineraryItem(
                        id = 2, tripId = tripId, type = PlanType.HOTEL,
                        locationName = "Pullman Paris Tour Eiffel",
                        address = "18 Avenue de Suffren",
                        checkInDate = dIn, checkOutDate = dOut,
                        price = 1400.0
                    ),
                    ItineraryItem(
                        id = 3, tripId = tripId, type = PlanType.LOCATION,
                        locationName = "Museu del Louvre",
                        address = "Rue de Rivoli, Paris",
                        checkInDate = calendar.apply { set(2026, Calendar.MAY, 16, 10, 0) }.time,
                        price = 17.0
                    ),
                    ItineraryItem(
                        id = 4, tripId = tripId, type = PlanType.RESTAURANT,
                        locationName = "Le Jules Verne",
                        address = "Torre Eiffel, 2on Pis",
                        checkInDate = calendar.apply { set(2026, Calendar.MAY, 16, 20, 0) }.time,
                        price = 250.0
                    )
                )
                TripItineraryInfo("París", R.drawable.paris, dIn, dOut, items)
            }
            3 -> { // NOVA YORK
                val dIn = calendar.apply { set(2026, Calendar.AUGUST, 10, 12, 0) }.time
                val dOut = calendar.apply { set(2026, Calendar.AUGUST, 25, 11, 0) }.time

                val items = listOf(
                    ItineraryItem(
                        id = 1, tripId = tripId, type = PlanType.FLIGHT,
                        company = "Delta Airlines", transportNumber = "DL201",
                        origin = "Barcelona", destination = "Aeroport JFK",
                        departureDate = dIn,
                        price = 650.0
                    ),
                    ItineraryItem(
                        id = 2, tripId = tripId, type = PlanType.HOTEL,
                        locationName = "Marriott Marquis",
                        address = "Times Square, NY",
                        checkInDate = dIn, checkOutDate = dOut,
                        price = 2800.0
                    ),
                    ItineraryItem(
                        id = 3, tripId = tripId, type = PlanType.LOCATION,
                        locationName = "Estàtua de la Llibertat",
                        address = "Liberty Island",
                        checkInDate = calendar.apply { set(2026, Calendar.AUGUST, 11, 9, 30) }.time,
                        price = 25.0
                    ),
                    ItineraryItem(
                        id = 4, tripId = tripId, type = PlanType.RESTAURANT,
                        locationName = "Joe's Pizza",
                        address = "Greenwich Village",
                        checkInDate = calendar.apply { set(2026, Calendar.AUGUST, 11, 13, 0) }.time,
                        price = 15.0
                    )
                )
                TripItineraryInfo("Nova York", R.drawable.ny, dIn, dOut, items)
            }
            else -> { // KYOTO
                val dIn = calendar.apply { set(2026, Calendar.MARCH, 23, 10, 30) }.time
                val dOut = calendar.apply { set(2026, Calendar.MARCH, 30, 15, 0) }.time

                val items = listOf(
                    ItineraryItem(
                        id = 1, tripId = tripId, type = PlanType.FLIGHT,
                        company = "Japan Airlines", transportNumber = "JL123",
                        origin = "Barcelona", destination = "Aeroport de Narita",
                        departureDate = dIn,
                        price = 850.0
                    ),
                    ItineraryItem(
                        id = 2, tripId = tripId, type = PlanType.HOTEL,
                        locationName = "Shinjuku Granbell Hotel",
                        address = "2-14-5 Kabukicho, Shinjuku-ku",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 23, 15, 0) }.time,
                        checkOutDate = calendar.apply { set(2026, Calendar.MARCH, 30, 11, 0) }.time,
                        price = 1200.0
                    ),
                    ItineraryItem(
                        id = 3, tripId = tripId, type = PlanType.LOCATION,
                        locationName = "Temple Senso-ji",
                        address = "2-3-1 Asakusa, Taito, Tòquio",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 24, 10, 0) }.time,
                        price = 0.0
                    ),
                    ItineraryItem(
                        id = 4, tripId = tripId, type = PlanType.RESTAURANT,
                        locationName = "Ichiran Ramen Shinjuku",
                        address = "3-34-11 Shinjuku, Tòquio",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 24, 19, 0) }.time,
                        price = 15.0
                    ),
                    ItineraryItem(
                        id = 5, tripId = tripId, type = PlanType.TRAIN,
                        company = "JR Central", transportNumber = "Nozomi 215",
                        origin = "Tòquio", destination = "Kyoto",
                        departureDate = calendar.apply { set(2026, Calendar.MARCH, 25, 9, 30) }.time,
                        price = 90.0
                    ),
                    ItineraryItem(
                        id = 11, tripId = tripId, type = PlanType.BOAT,
                        company = "JR West Ferry", transportNumber = "Miyajima Line",
                        origin = "Hiroshima", destination = "Miyajima",
                        departureDate = calendar.apply { set(2026, Calendar.MARCH, 26, 10, 0) }.time,
                        price = 5.0
                    ),
                    ItineraryItem(
                        id = 12, tripId = tripId, type = PlanType.TRAIN,
                        company = "Odakyu Railways", transportNumber = "EXEα 30000",
                        origin = "Shinjuku", destination = "Hakone",
                        departureDate = calendar.apply { set(2026, Calendar.MARCH, 27, 8, 45) }.time,
                        price = 22.0
                    ),
                    ItineraryItem(
                        id = 6, tripId = tripId, type = PlanType.RESTAURANT,
                        locationName = "Gion Karyo",
                        address = "570-235 Gionmachi, Kyoto",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 25, 20, 30) }.time,
                        price = 120.5
                    ),
                    ItineraryItem(
                        id = 7, tripId = tripId, type = PlanType.LOCATION,
                        locationName = "Shibuya Sky Mirador",
                        address = "2-24-12 Shibuya, Tòquio",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 26, 12, 0) }.time,
                        price = 25.0
                    ),
                    ItineraryItem(
                        id = 8, tripId = tripId, type = PlanType.LOCATION,
                        locationName = "Fushimi Inari Taisha",
                        address = "68 Fukakusa Yabunouchicho, Kyoto",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 27, 11, 0) }.time,
                        price = 0.0
                    ),
                    ItineraryItem(
                        id = 10, tripId = tripId, type = PlanType.LOCATION,
                        locationName = "Parc dels Cérvols de Nara",
                        address = "Nara, Japó",
                        checkInDate = calendar.apply { set(2026, Calendar.MARCH, 29, 13, 0) }.time,
                        price = 10.0
                    ),
                )
                TripItineraryInfo("Kyoto", R.drawable.kyoto_2, dIn, dOut, items)
            }
        }
    }

    val groupedData = mockData
        .sortedBy { it.getInDate() }
        .groupBy { it.formatDateKey(it.getInDate()!!) }

    val numItems = mockData.size

    var showPopUp by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { MyTopBar(
            onBackClick = { navController.popBackStack() },
            menuItems = listOf(
                TopBarAction(
                    stringResource(R.string.deleteTrip),
                    onClick = {
                        showPopUp = true
                    }
                    ),
            ))
            },
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
            ) {
                SmallFloatingActionButton (onClick = { navController.navigate("album/$tripId") }) {
                    Icon(imageVector = Icons.Filled.PhotoAlbum, contentDescription = null)
                }
                FloatingActionButton(onClick = { navController.navigate("plan") }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        PopUp(show = showPopUp, title = stringResource(R.string.deleteTrip), text = stringResource(R.string.popUp_deleteTrip_text), onAccept = { showPopUp = false }, onDismiss = { showPopUp = false })
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Header(destinationName, dateIn, dateOut, headerImg)
            }

            if (numItems == 0) {
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
                item {
                    ItineraryStatsComponent(mockData.sumOf { it.price }, numItems)
                    Spacer(modifier = Modifier.size(AppDimensions.PaddingMedium))
                }
                groupedData.forEach { (date, itemsDelDia) ->
                    item {
                        DivisorComponent(date)
                        Spacer(modifier = Modifier.size(AppDimensions.PaddingSmall))
                    }
                    items(itemsDelDia) { plan ->
                        ItineraryItemComponent(plan)
                        Spacer(modifier = Modifier.size(AppDimensions.PaddingSmall))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.size(innerPadding.calculateBottomPadding()))
            }
        }
    }
}

data class TripItineraryInfo(
    val destination: String,
    val imageRes: Int,
    val startDate: Date,
    val endDate: Date,
    val plans: List<ItineraryItem>
)

@Composable
fun Header(destination: String, startDate: Date, endDate: Date, imageRes: Int) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .blur(radius = 3.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier
                .padding(start = AppDimensions.PaddingMedium, bottom = AppDimensions.PaddingSmall)
        ) {
            Text(
                text = stringResource(R.string.trip_to, destination),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
            )
            val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
            val tripLength = TimeUnit.MILLISECONDS.toDays(endDate.time - startDate.time)
            Text(
                text = "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)} • $tripLength dies de durada",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
        }
    }
}

@Composable
fun ItineraryStatsComponent(budget: Double, items: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            label = "Pressupost",
            value = "$budget€",
            modifier = Modifier.weight(1f)
        )
        StatItem(
            label = "Activitats",
            value = "$items",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(0.5.dp, MaterialTheme.colorScheme.outline)
            .padding(AppDimensions.PaddingSmall),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun DivisorComponent(date: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = AppDimensions.PaddingMedium)
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = date,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ItineraryItemComponent(
    ico: ImageVector,
    color: Color,
    background: Color,
    enterTime: String,
    title: String,
    secondaryText: String,
    tertiaryText: String = "",
    onLongClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimensions.PaddingMedium)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
            modifier = Modifier.padding(horizontal =  AppDimensions.PaddingMedium, vertical = AppDimensions.PaddingSmall)
        ) {
            Text(
                text = enterTime,
                style = MaterialTheme.typography.labelLarge
            )

            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = background
            ) {
                Icon(
                    imageVector = ico,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(AppDimensions.PaddingSmall)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(text = secondaryText, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = tertiaryText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ItineraryItemComponent(item: ItineraryItem) {
    var showPopUp by remember { mutableStateOf(false) }

    when (item.type.route) {
        "flight","boat","train" -> ItineraryItemComponent(
            item.type.icon,
            item.type.iconColor,
            item.type.backgroundColor,
            item.getDateInTime(),
            "${item.origin ?: "-"} -> ${item.destination ?: "-"}",
            "${item.transportNumber ?: "-"} (${item.company ?: "-"})",
            tertiaryText = "${item.price}€",
            onLongClick = { showPopUp = true }
        )
        "hotel","location","restaurant" -> ItineraryItemComponent(
            item.type.icon,
            item.type.iconColor,
            item.type.backgroundColor,
            item.getDateInTime(),
            item.locationName ?: "",
            item.address ?: "",
            tertiaryText = "${item.price}€",
            onLongClick = { showPopUp = true }
        )
    }

    PopUp(show = showPopUp, title = "Eliminar plan", text = "¿Estàs segur que vols eliminar aquest plan del teu itinerari?", acceptText = stringResource(R.string.delete), onAccept = { showPopUp = false }, onDismiss = { showPopUp = false })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItineraryPreview() {
    ItineraryScreen(rememberNavController(),1)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(rememberNavController(),3)
}