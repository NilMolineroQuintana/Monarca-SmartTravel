package com.example.monarcasmarttravel.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Upload
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.Image
import com.example.monarcasmarttravel.domain.ItineraryItem
import com.example.monarcasmarttravel.domain.PlanType
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.WideOption
import java.util.Calendar

@Composable
fun ItineraryScreen(navController: NavController) {
    // Mock-up data

    val calendar = Calendar.getInstance()

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

    val mockData = listOf(
        ItineraryItem(id = 1, type = PlanType.FLIGHT, company = "Japan Airlines", locationName = "Narita Airport (NRT)", checkInDate = dataVol, price = 850.0, transportNumber = "JL123"),

        ItineraryItem(id = 2, type = PlanType.HOTEL, locationName = "Shinjuku Granbell Hotel", checkInDate = checkInHotel, checkOutDate = checkOutGeneral, price = 1200.0, address = "2-14-5 Kabukicho, Shinjuku-ku, Tokyo"),

        ItineraryItem(id = 3, type = PlanType.LOCATION, locationName = "Templo Senso-ji", checkInDate = dataSensoji, price = 0.0, address = "2-3-1 Asakusa, Taito City, Tokyo"),

        ItineraryItem(id = 4, type = PlanType.RESTAURANT, locationName = "Ichiran Ramen Shinjuku", checkInDate = dataCenaShinjuku, price = 15.0, address = "3-34-11 Shinjuku, Tokyo"),

        ItineraryItem(id = 5, type = PlanType.FLIGHT, company = "JR Central", locationName = "Shinkansen: Tokyo -> Kyoto", checkInDate = dataShinkansenKyoto, price = 90.0, transportNumber = "Nozomi 215"),

        ItineraryItem(id = 6, type = PlanType.RESTAURANT, locationName = "Gion Karyo", checkInDate = dataRest, price = 120.0, address = "570-235 Gionmachi Minamigawa, Higashiyama Ward, Kyoto"),

        ItineraryItem(id = 7, type = PlanType.LOCATION, locationName = "Shibuya Sky Mirador", checkInDate = dataMirador, price = 25.0, address = "2-24-12 Shibuya, Tokyo"),

        ItineraryItem(id = 8, type = PlanType.LOCATION, locationName = "Fushimi Inari Taisha", checkInDate = dataFushimiInari, price = 0.0, address = "68 Fukakusa Yabunouchicho, Fushimi Ward, Kyoto"),

        ItineraryItem(id = 9, type = PlanType.LOCATION, locationName = "Kurama Onsen Spa", checkInDate = dataOnsen, price = 45.0, address = "520 Kuramahonmachi, Sakyo Ward, Kyoto"),

        ItineraryItem(id = 10, type = PlanType.LOCATION, locationName = "Nara Deer Park", checkInDate = dataNaraPark, price = 10.0, address = "Nara, Prefectura de Nara, Japón")
    )
    val groupedData = mockData
        .sortedBy { it.checkInDate }
        .groupBy { it.formatDateKey(it.checkInDate) }

    // Mock-up data

    Scaffold(
        topBar = { MyTopBar(showPageTitle = false, onBackClick = { navController.popBackStack() }) },
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
        LazyColumn (
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kyoto_2),
                        contentDescription = "Imagen con blur",
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
                            text = "Viaje a Kioto",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                        )
                        Text(
                            text = "23 Mar - 30 Mar • x dies",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.size(AppDimensions.PaddingLarge))
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
                    item {
                        Spacer(modifier = Modifier.size(AppDimensions.PaddingMedium))
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

@Composable
fun PlanOptionsScreen(navController: NavController) {

    val PopularPlans = listOf(PlanType.FLIGHT, PlanType.BOAT, PlanType.TRAIN,)
    val MorePlans = listOf(PlanType.HOTEL, PlanType.RESTAURANT, PlanType.LOCATION)

    Scaffold(
        topBar = { MyTopBar("Afegir plan", onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            item { Text(
                text = "Els més populars",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            val mod = Modifier.padding(vertical = 4.dp)

            items(PopularPlans) { plan ->
                WideOption(plan.icon, stringResource(id = plan.titleRes), showIcon = false, modifier = mod, onClick = { navController.navigate("plan/${plan.route}") })
            }

            item {
                Text (
                    text = "Més",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            items(MorePlans) { plan ->
                WideOption(plan.icon, stringResource(id = plan.titleRes), showIcon = false, modifier = mod, onClick = { navController.navigate("plan/${plan.route}") })
            }
        }
    }
}

@Composable
fun AlbumScreen(navController: NavController) {
    val mockData = listOf(
        Image(id = 1, image_id = R.drawable.kyoto, dateUploaded = Calendar.getInstance().time),
        Image(id = 2, image_id = R.drawable.kyoto_2, dateUploaded = Calendar.getInstance().time),
        Image(id = 3, image_id = R.drawable.kyoto_3, dateUploaded = Calendar.getInstance().time),
        Image(id = 4, image_id = R.drawable.kyoto_4, dateUploaded = Calendar.getInstance().time)
    )

    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<Image?>(null) }

    Scaffold(
        topBar = { MyTopBar("Álbum", onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(
                    context,
                    "Funció que s'implementarà més endevant",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(imageVector = Icons.Filled.Upload, contentDescription = null)
            }
        }
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = innerPadding,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(mockData) { img ->
                Image(
                    painter = painterResource(id = img.image_id),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { selectedImage = img },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    selectedImage?.let { img ->
        Dialog(
            onDismissRequest = { selectedImage = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = img.image_id),
                    contentDescription = "Imagen maximizada",
                    modifier = Modifier.fillMaxSize(0.95f),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun ItineraryItemComponent(ico: ImageVector, enterTime: String, title: String, secondaryText: String, tertiaryText: String = "")
{
    Surface(
        modifier = Modifier.padding(horizontal = AppDimensions.PaddingMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium),
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(
                text = enterTime,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(25.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
            ) {
                Icon(
                    imageVector = ico,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
            Column() {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = secondaryText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = tertiaryText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ItineraryItemComponent(item: ItineraryItem) {
    when (item.type.route) {
        "flight","boat","train" -> ItineraryItemComponent(
            item.type.icon,
            item.getCheckInTime(),
            "Origen: ${item.locationName}",
            "${item.transportNumber} (${item.company})",
            tertiaryText = "${item.price}€"
        )
        "hotel","location","restaurant" -> ItineraryItemComponent(
            item.type.icon,
            item.getCheckInTime(),
            item.locationName,
            item.address,
            tertiaryText = "${item.price}€"
        )
    }
}

@Composable
fun ImageComponent(image: Image) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Image(
                painter = painterResource(id = image.image_id),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = AppDimensions.PaddingMedium)
                    .padding(top = AppDimensions.PaddingMedium)
                    .clip(RoundedCornerShape(12.dp))
            )
            Text(
                text = image.dateUploaded.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppDimensions.PaddingSmall ,end = AppDimensions.PaddingMedium, bottom = AppDimensions.PaddingSmall)
            )
        }
    }
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

@Preview
@Composable
fun ItineraryItemPreview() {
    ItineraryItemComponent(Icons.Filled.Hotel,"Sakura Mori Retreat", "23/03/2026 18:55", "3-chōme-43-15 Sendagi, Bunkyo City, Tokyo 113-0022, Japón")
}