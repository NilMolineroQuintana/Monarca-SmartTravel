package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.monarcasmarttravel.ui.MyBottomBar

@Composable
fun NotificationsScreen(navController: NavController) {
    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Text("Create Notifications", modifier = Modifier.padding(innerPadding))
    }
}