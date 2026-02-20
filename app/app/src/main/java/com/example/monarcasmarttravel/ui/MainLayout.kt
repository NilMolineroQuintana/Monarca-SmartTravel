package com.example.monarcasmarttravel.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.monarcasmarttravel.R

@Composable
fun MyBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem (
            selected = currentRoute == "home",
            onClick = { if (currentRoute != "home") navController.navigate("home") },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_home)) }
        )
        NavigationBarItem(
            selected = currentRoute == "trips",
            onClick = {if (currentRoute != "trips") navController.navigate("trips") },
            icon = { Icon(imageVector = Icons.Filled.Luggage, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_trips)) }
        )
        NavigationBarItem(
            selected = currentRoute == "notifications",
            onClick = { if (currentRoute != "notifications") navController.navigate("notifications") },
            icon = { Icon(imageVector = Icons.Filled.Notifications, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_notifications)) }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { if (currentRoute != "profile") navController.navigate("profile") },
            icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null) },
            label = { Text(text = stringResource(R.string.bottom_menu_profile)) }
        )
    }
}

@Composable
fun WideOption(ico: ImageVector, text: String, rounded: Boolean = true, secondaryText: String = "",onClick: () -> Unit) {
    val shape = if (rounded) RoundedCornerShape(12.dp) else RectangleShape
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = ico,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!secondaryText.isEmpty())
                {
                    Text(
                        text = secondaryText,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.ArrowCircleRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun PreviewWideOption() {
    WideOption(Icons.Filled.Settings, "Configuració",onClick = {})
}

@Preview
@Composable
fun PreviewWideOptionSecondary() {
    WideOption(Icons.Filled.Settings, "Configuració", secondaryText = "Test",onClick = {})
}

@Preview
@Composable
fun PreviewWideSquaredOption() {
    WideOption(Icons.Filled.Settings, "Configuració", rounded = false, onClick = {})
}

@Preview
@Composable
fun PreviewWideOptionSquaredSecondary() {
    WideOption(Icons.Filled.Settings, "Configuració", secondaryText = "Test", rounded = false, onClick = {})
}