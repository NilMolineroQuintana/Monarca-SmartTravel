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
import androidx.compose.material.icons.filled.PhotoAlbum
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
import com.example.monarcasmarttravel.domain.PlanType
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.PopUp
import com.example.monarcasmarttravel.ui.TopBarAction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

val calendar = Calendar.getInstance()

@Composable
fun ItineraryScreen(navController: NavController) {
    // Mock-up data

    calendar.set(2026, Calendar.MARCH, 23, 10, 30)
    val dataVol = calendar.time

    calendar.set(2026, Calendar.MARCH, 23, 15, 0)
    val checkInHotel = calendar.time

    calendar.set(2026, Calendar.MARCH, 24, 10, 0)
    val dataSensoji = calendar.time

    calendar.set(2026, Calendar.MARCH, 24, 19, 0)
    val dataCenaShinjuku = calendar.time

    calendar.set(2026, Calendar.MARCH, 25, 9, 30)
    val dataShinkansenKyoto = calendar.time

    calendar.set(2026, Calendar.MARCH, 25, 20, 30)
    val dataRest = calendar.time

    calendar.set(2026, Calendar.MARCH, 26, 12, 0)
    val dataMirador = calendar.time

    calendar.set(2026, Calendar.MARCH, 27, 11, 0)
    val dataFushimiInari = calendar.time

    calendar.set(2026, Calendar.MARCH, 28, 18, 0)
    val dataOnsen = calendar.time

    calendar.set(2026, Calendar.MARCH, 29, 13, 0)
    val dataNaraPark = calendar.time

    calendar.set(2026, Calendar.MARCH, 30, 11, 0)
    val checkOutGeneral = calendar.time

    calendar.set(2026, Calendar.MARCH, 26, 10, 0)
    val dataFerryMiyajima = calendar.time

    calendar.set(2026, Calendar.MARCH, 27, 8, 45)
    val dataTrenLimitedExp = calendar.time

    val mockData = listOf(
        ItineraryItem(id = 1, type = PlanType.FLIGHT, company = "Japan Airlines", locationName = "Narita Airport", checkInDate = dataVol, price = 850.0, transportNumber = "JL123"),

        ItineraryItem(id = 2, type = PlanType.HOTEL, locationName = "Shinjuku Granbell Hotel", checkInDate = checkInHotel, checkOutDate = checkOutGeneral, price = 1200.0, address = "2-14-5 Kabukicho, Shinjuku-ku"),

        ItineraryItem(id = 3, type = PlanType.LOCATION, locationName = "Templo Senso-ji", checkInDate = dataSensoji, price = 0.0, address = "2-3-1 Asakusa, Taito City, Tokyo"),

        ItineraryItem(id = 4, type = PlanType.RESTAURANT, locationName = "Ichiran Ramen Shinjuku", checkInDate = dataCenaShinjuku, price = 15.0, address = "3-34-11 Shinjuku, Tokyo"),

        ItineraryItem(id = 5, type = PlanType.TRAIN, company = "JR Central", locationName = "Tokyo", checkInDate = dataShinkansenKyoto, price = 90.0, transportNumber = "Nozomi 215"),

        ItineraryItem(id = 11, type = PlanType.BOAT, company = "JR West Ferry", locationName = "Miyajima", checkInDate = dataFerryMiyajima, price = 5.0, transportNumber = "Miyajima Line"),

        ItineraryItem(id = 12, type = PlanType.TRAIN, company = "Odakyu Railways", locationName = "Shinjuku", checkInDate = dataTrenLimitedExp, price = 22.0, transportNumber = "EXEα 30000"),

        ItineraryItem(id = 6, type = PlanType.RESTAURANT, locationName = "Gion Karyo", checkInDate = dataRest, price = 120.5, address = "570-235 Gionmachi Minamigawa, Higashiyama Ward, Kyoto"),

        ItineraryItem(id = 7, type = PlanType.LOCATION, locationName = "Shibuya Sky Mirador", checkInDate = dataMirador, price = 25.0, address = "2-24-12 Shibuya, Tokyo"),

        ItineraryItem(id = 8, type = PlanType.LOCATION, locationName = "Fushimi Inari Taisha", checkInDate = dataFushimiInari, price = 0.0, address = "68 Fukakusa Yabunouchicho, Fushimi Ward, Kyoto"),

        ItineraryItem(id = 9, type = PlanType.LOCATION, locationName = "Kurama Onsen Spa", checkInDate = dataOnsen, price = 45.0, address = "520 Kuramahonmachi, Sakyo Ward, Kyoto"),

        ItineraryItem(id = 10, type = PlanType.LOCATION, locationName = "Nara Deer Park", checkInDate = dataNaraPark, price = 10.0, address = "Nara, Prefectura de Nara, Japón"),
    )
    val groupedData = mockData
        .sortedBy { it.checkInDate }
        .groupBy { it.formatDateKey(it.checkInDate) }

    // Mock-up data

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
                SmallFloatingActionButton (onClick = { navController.navigate("album") }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                calendar.set(2026, Calendar.MARCH, 23)
                val dateIn = calendar.time
                calendar.set(2026, Calendar.MARCH, 30)
                val dateOut = calendar.time
                Header("Kioto", dateIn, dateOut)
            }
            item {
                ItineraryStatsComponent(mockData.sumOf { it.price }, mockData)
                Spacer(modifier = Modifier.size(AppDimensions.PaddingMedium))
            }

            if (false) {
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

// PER IMPLEMENTAR: Aqui li hauriem de passar un data class de tipus trip.
@Composable
fun Header(destination: String, startDate: java.util.Date, endDate: java.util.Date) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.kyoto_2),
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
            val dateInString = dateFormat.format(startDate)
            val dateOutString = dateFormat.format(endDate)
            val tripLength = TimeUnit.MILLISECONDS.toDays(endDate.time - startDate.time)
            Text(
                text = "${dateInString} - ${dateOutString} • ${tripLength} dies de duració",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
        }
    }
}

@Composable
fun ItineraryStatsComponent(budget: Double, data: List<ItineraryItem>) {
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
            value = "${data.size}",
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
            item.getCheckInTime(),
            "Origen: ${item.locationName}",
            "${item.transportNumber} (${item.company})",
            tertiaryText = "${item.price}€",
            onLongClick = { showPopUp = true }
        )
        "hotel","location","restaurant" -> ItineraryItemComponent(
            item.type.icon,
            item.type.iconColor,
            item.type.backgroundColor,
            item.getCheckInTime(),
            item.locationName,
            item.address,
            tertiaryText = "${item.price}€",
            onLongClick = { showPopUp = true }
        )
    }

    PopUp(show = showPopUp, title = "Eliminar plan", text = "¿Estàs segur que vols eliminar aquest plan del teu itinerari?", acceptText = stringResource(R.string.delete), onAccept = { showPopUp = false }, onDismiss = { showPopUp = false })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItineraryPreview() {
    ItineraryScreen(rememberNavController())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlanScreenPreview() {
    PlanOptionsScreen(rememberNavController())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(rememberNavController())
}