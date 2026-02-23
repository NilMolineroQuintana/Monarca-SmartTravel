package com.example.monarcasmarttravel.ui.screens.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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

@Composable
fun TransportScreen(navController: NavController) {
    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.plan_flight), onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                TextField(value = "", onValueChange = {}, label = { Text(stringResource(R.string.data_de_sortida)) }, modifier = Modifier.padding(top = AppDimensions.PaddingLarge))
            }
            item {
                TextField(value = "", onValueChange = {}, label = { Text("Companyia") })
            }
            item {
                TextField(value = "", onValueChange = {}, label = { Text("Número de vol") })
            }
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 20.dp)
                ) {
                    TextButton(onClick = {}) { Text(text = "Afegir vol") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightScreenPreview() {
    TransportScreen(rememberNavController())
}