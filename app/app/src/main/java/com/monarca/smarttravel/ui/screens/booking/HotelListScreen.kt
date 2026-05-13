package com.monarca.smarttravel.ui.screens.booking

import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.monarca.smarttravel.R
import com.monarca.smarttravel.domain.model.Hotel
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.MyBottomBar
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.viewmodels.BookingViewModel
import com.monarca.smarttravel.utils.Constants

@Composable
fun HotelListScreen(navController: NavController) {

    val parentEntry = remember(navController) {
        navController.getBackStackEntry("book")
    }
    val viewModel: BookingViewModel = hiltViewModel(parentEntry)

    val hotels by viewModel.hotels.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { MyTopBar(title = "Llista de hotels", onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            items(hotels ?: emptyList()) { hotel ->
                HotelComponent(hotel)
            }

            item {
                Spacer(modifier = Modifier.size(innerPadding.calculateBottomPadding()))
            }
        }
    }

}

@Composable
fun HotelComponent(hotel: Hotel) {

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(100.dp)
        ) {
            AsyncImage(
                model = Constants.BASE_URL + hotel.image_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.paris),
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(AppDimensions.PaddingMedium)
            ) {
                Text(
                    text = hotel.name, fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = Constants.marqueeRepeat,
                        initialDelayMillis = Constants.marqueeDelay
                    )
                )
                Text(
                    text = hotel.address,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = Constants.marqueeRepeat,
                        initialDelayMillis = Constants.marqueeDelay
                    )
                )
                var ratingStr = ""
                repeat(hotel.rating) { ratingStr += "⭐" }
                Text(text = ratingStr)
            }
        }
    }
}

@Preview
@Composable
fun HotelComponentPreview() {
    HotelComponent(
        Hotel(
            id = "1",
            name = "Hotel 1",
            address = "Address 1",
            rating = 3,
            image_url = "",
            rooms = emptyList()
        )
    )
}