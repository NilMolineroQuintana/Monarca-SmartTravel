package com.example.monarcasmarttravel.ui.screens.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyTopBar
import java.util.Calendar

@Composable
fun HotelScreen(navController: NavController) {

    val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(Calendar.getInstance().time)
    var accommodationName by remember { mutableStateOf("") }
    var checkInDate by remember { mutableStateOf(date) }
    var checkOutDate by remember { mutableStateOf(date) }
    var address by remember { mutableStateOf("") }

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.plan_hotel), onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            item {
                TextField(
                    value = accommodationName,
                    onValueChange = { accommodationName = it },
                    label = { Text(stringResource(R.string.hotel_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.direccio)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = checkInDate,
                        onValueChange = { checkInDate = it },
                        label = { Text(stringResource(R.string.data_de_entrada)) },
                        modifier = Modifier.weight(1f)
                    )
                    TextField(
                        value = checkOutDate,
                        onValueChange = { checkOutDate = it },
                        label = { Text(stringResource(R.string.data_de_sortida)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    shape = RoundedCornerShape(20.dp),
                    onClick = { },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(stringResource(R.string.add))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotelScreenPreview() {
    HotelScreen(rememberNavController())
}