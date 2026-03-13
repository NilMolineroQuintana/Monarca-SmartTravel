package com.example.monarcasmarttravel.ui.screens.trip

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBoatFilled
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.OptionsPopUp
import com.example.monarcasmarttravel.ui.PopUp
import com.example.monarcasmarttravel.ui.TopBarAction
import com.example.monarcasmarttravel.ui.viewmodels.ItineraryViewModel
import com.example.monarcasmarttravel.ui.viewmodels.TripViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Enum que representa els tipus de plans que es poden afegir a un itinerari.
 *
 * Cada tipus té associat: el recurs de text del títol, la icona, la ruta de navegació,
 * i els colors de fons i icona per a la targeta de l'itinerari.
 */
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

/**
 * Pantalla de l'itinerari d'un viatge concret.
 *
 * Obté les dades reals del viatge (títol, imatge, dates) des del [TripViewModel],
 * en lloc d'usar dades hardcoded. Si el viatge no existeix, mostra un fallback.
 *
 * @param navController Controlador de navegació.
 * @param tripId Identificador del viatge a mostrar.
 */
@Composable
fun ItineraryScreen(navController: NavController, tripId: Int) {

    val itineraryViewModel: ItineraryViewModel = hiltViewModel()
    val tripViewModel: TripViewModel = hiltViewModel()

    // Obté el viatge real del repositori a través del ViewModel
    val trip = tripViewModel.getTripById(tripId)

    // Fallback per si el viatge no s'ha trobat (no hauria de passar en condicions normals)
    val destinationName = trip?.title ?: "Viatge"
    val dateIn = trip?.dateIn ?: Date()
    val dateOut = trip?.dateOut ?: Date()
    val headerImg = trip?.imageResId ?: R.drawable.kyoto_2

    val items by itineraryViewModel.items.collectAsState()
    val isLoading by itineraryViewModel.isLoading.collectAsState()

    LaunchedEffect(tripId) {
        itineraryViewModel.loadItemsByTrip(tripId)
    }

    val groupedData = items
        .sortedBy { it.getInDate() }
        .filter { it.getInDate() != null }
        .groupBy { it.formatDateKey(it.getInDate()!!) }

    val numItems = items.size
    var showPopUp by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopBar(
                onBackClick = { navController.popBackStack() },
                menuItems = listOf(
                    TopBarAction(
                        stringResource(R.string.deleteTrip),
                        onClick = { showPopUp = true }
                    ),
                )
            )
        },
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
            ) {
                // Botó petit per accedir a l'àlbum de fotos del viatge
                SmallFloatingActionButton(onClick = { navController.navigate("album/$tripId") }) {
                    Icon(imageVector = Icons.Filled.PhotoAlbum, contentDescription = null)
                }
                // Botó principal per afegir un nou pla a l'itinerari
                FloatingActionButton(onClick = { navController.navigate("plan/$tripId") }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        // PopUp de confirmació per eliminar el viatge
        PopUp(
            show = showPopUp,
            title = stringResource(R.string.deleteTrip),
            text = stringResource(R.string.popUp_deleteTrip_text),
            onAccept = {
                // Elimina el viatge via ViewModel i torna a la llista
                tripViewModel.deleteTrip(tripId)
                showPopUp = false
                navController.navigate("trips") {
                    popUpTo("trips") { inclusive = true }
                }
            },
            onDismiss = { showPopUp = false }
        )

        // ← AQUÍ va el control de loading, dentro del Scaffold
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item { Header(destinationName, dateIn, dateOut, headerImg) }

                if (numItems == 0) {
                    item {
                        Text(
                            text = stringResource(R.string.no_plans),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                        )
                    }
                } else {
                    item {
                        ItineraryStatsComponent(items.sumOf { it.price }, numItems)
                        Spacer(modifier = Modifier.size(AppDimensions.PaddingMedium))
                    }
                    groupedData.forEach { (date, itemsDelDia) ->
                        item {
                            DivisorComponent(date)
                            Spacer(modifier = Modifier.size(AppDimensions.PaddingSmall))
                        }
                        items(itemsDelDia) { plan ->
                            ItineraryItemComponent(item = plan, onDelete = {
                                itineraryViewModel.deleteItem(plan)
                                itineraryViewModel.loadItemsByTrip(tripId)
                            }, navController = navController
                            )
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
}

/**
 * Capçalera visual de l'itinerari amb la imatge del destí com a fons desenfocada,
 * un degradat fosc a la part inferior, i el nom del destí amb les dates del viatge.
 */
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

        // Degradat vertical per millorar la llegibilitat del text sobre la imatge
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
                color = Color.White
            )
            val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
            val tripLength = TimeUnit.MILLISECONDS.toDays(endDate.time - startDate.time)
            Text(
                text = stringResource(
                    R.string.days_of_duration,
                    dateFormat.format(startDate),
                    dateFormat.format(endDate),
                    tripLength
                ),
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
        }
    }
}

/**
 * Component que mostra les estadístiques globals del viatge:
 * pressupost total i nombre total de plans afegits.
 */
@Composable
fun ItineraryStatsComponent(budget: Double, items: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            label = stringResource(R.string.budget),
            value = "$budget€",
            modifier = Modifier.weight(1f)
        )
        StatItem(
            label = stringResource(R.string.activities),
            value = "$items",
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Element individual d'estadística amb un valor destacat i una etiqueta descriptiva.
 */
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

/**
 * Separador visual entre grups de plans del mateix dia.
 * Mostra la data formatada dins d'una píndola de color primari.
 */
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = date,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/**
 * Component visual base per a un element de l'itinerari.
 *
 * Mostra la icona del tipus de pla, l'hora d'entrada, el títol, text secundari
 * i un text terciari (normalment el preu). Amb clic llarg s'activa [onLongClick].
 *
 * @param ico Icona del tipus de pla.
 * @param color Color de la icona.
 * @param background Color de fons del contenidor de la icona.
 * @param enterTime Hora d'entrada o sortida formatada (HH:mm).
 * @param title Text principal (nom del lloc o ruta de transport).
 * @param secondaryText Text secundari (adreça o número de vol/tren/vaixell).
 * @param tertiaryText Text terciari, normalment el preu. Opcional.
 * @param onLongClick Acció en fer clic llarg sobre l'element.
 */
@OptIn(ExperimentalFoundationApi::class)
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
            modifier = Modifier.padding(horizontal = AppDimensions.PaddingMedium, vertical = AppDimensions.PaddingSmall)
        ) {
            Text(
                text = enterTime,
                style = MaterialTheme.typography.labelLarge
            )

            // Contenidor de la icona amb color de fons personalitzat per tipus de pla
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

            Column(modifier = Modifier.weight(1f)) {
                // Títol amb efecte de desplaçament horitzontal si és massa llarg
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = 1000,
                        initialDelayMillis = 5000
                    )
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

/**
 * Sobrecàrrega de [ItineraryItemComponent] que accepta directament un [ItineraryItem]
 * i decideix el format de visualització segons el tipus de pla (transport o allotjament/lloc).
 * Inclou el diàleg de confirmació per eliminar el pla.
 */
@Composable
fun ItineraryItemComponent(item: ItineraryItem, onDelete: () -> Unit = { }, navController: NavController) {
    var showPopUp by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    when (item.type.route) {
        // Transport: mostra origen → destí i número de vehicle
        "flight", "boat", "train" -> ItineraryItemComponent(
            item.type.icon,
            item.type.iconColor,
            item.type.backgroundColor,
            item.getDateInTime(),
            "${item.origin ?: "-"} -> ${item.destination ?: "-"}",
            "${item.transportNumber ?: "-"} (${item.company ?: "-"})",
            tertiaryText = "${item.price}€",
            onLongClick = { showOptions = true }
        )
        // Allotjament / punt d'interès: mostra nom i adreça
        "hotel", "location", "restaurant" -> ItineraryItemComponent(
            item.type.icon,
            item.type.iconColor,
            item.type.backgroundColor,
            item.getDateInTime(),
            item.locationName ?: "",
            item.address ?: "",
            tertiaryText = "${item.price}€",
            onLongClick = { showOptions = true }
        )
    }

    PopUp(
        show = showPopUp,
        title = stringResource(R.string.delete_plan),
        text = stringResource(R.string.delete_plan_description),
        acceptText = stringResource(R.string.delete),
        onAccept = {
            onDelete()
            showPopUp = false
        },
        onDismiss = { showPopUp = false }
    )

    OptionsPopUp(
        show = showOptions,
        title = "Selecciona una opció",
        options = listOf(
            Icons.Default.Edit    to "Editar",
            Icons.Default.Delete  to "Eliminar",
        ),
        onOptionSelected = { index ->
            when (index) {
                0 -> { navController.navigate("plan/${item.type.route}/${item.tripId}/${item.id}") }
                1 -> {
                    showOptions = false
                    showPopUp = true
                }
            }
        },
        onDismiss = { showOptions = false }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItineraryPreview() {
    ItineraryScreen(rememberNavController(), 1)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlbumScreenPreview2() {
    AlbumScreen(rememberNavController(), 3)
}