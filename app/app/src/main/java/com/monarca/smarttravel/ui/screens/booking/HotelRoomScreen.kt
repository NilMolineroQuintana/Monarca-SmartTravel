package com.monarca.smarttravel.ui.screens.booking

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.monarca.smarttravel.R
import com.monarca.smarttravel.domain.model.Room
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.MyBottomBar
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.viewmodels.BookingViewModel
import com.monarca.smarttravel.utils.Constants

@Composable
fun HotelRoomScreen(navController: NavController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry("book")
    }
    val viewModel: BookingViewModel = hiltViewModel(parentEntry)
    val hotel by viewModel.selectedHotel.collectAsStateWithLifecycle()
    val selectedRoom by viewModel.selectedRoom.collectAsStateWithLifecycle()

    var isDetailMode by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopBar(
                title = if (isDetailMode) selectedRoom?.room_type ?: "Detall" else hotel?.name ?: "Habitacions",
                onBackClick = {
                    if (isDetailMode) isDetailMode = false
                    else navController.popBackStack()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openMaps(navController.context, hotel?.address ?: "") }) {
                Icon(imageVector = Icons.Filled.Directions, contentDescription = null)
            }  },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxSize()
        ) {
            if (!isDetailMode) {
                hotel?.let { h ->
                    AsyncImage(
                        model = Constants.BASE_URL + h.image_url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.paris),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.PaddingMedium))
                    Text(
                        text = "Habitacions disponibles",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.PaddingSmall))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(h.rooms) { room ->
                            RoomComponent(room) {
                                viewModel.selectRoom(room)
                                isDetailMode = true
                            }
                        }
                    }
                }
            } else {
                selectedRoom?.let { room ->
                    RoomDetailContent(room) {
                        navController.navigate("bookConfirm")
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetailContent(room: Room, onReservar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            if (room.images.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = { room.images.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = Constants.BASE_URL + room.images[page],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.paris),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Text(
            text = room.room_type,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${room.price}€ / nit",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Aquesta habitació ofereix totes les comoditats necessàries per a una estada agradable, amb un disseny modern i funcional.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReservar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Reservar", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(AppDimensions.PaddingMedium))
    }
}

@Composable
fun RoomComponent(room: Room, onSelect: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            ) {
                if (room.images.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { room.images.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = Constants.BASE_URL + room.images[page],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            error = painterResource(id = R.drawable.paris),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    AsyncImage(
                        model = R.drawable.ic_launcher_foreground,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(AppDimensions.PaddingMedium)
            ) {
                Text(
                    text = room.room_type,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${room.price}€ / nit",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

fun openMaps(context: Context, address: String) {
    val encodedAddress = Uri.encode(address)
    val mapIntentUri = Uri.parse("geo:0,0?q=$encodedAddress")

    val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")

    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val genericIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        context.startActivity(genericIntent)
    }
}