package com.example.monarcasmarttravel.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.monarcasmarttravel.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


object AppDimensions {
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 24.dp
}
@Composable
fun MyTopBar(title: String = "", showPageTitle: Boolean = true, onBackClick: (() -> Unit)? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 40.dp, bottom = AppDimensions.PaddingMedium)
    ) {
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        if (showPageTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = (if (onBackClick == null) AppDimensions.PaddingMedium else AppDimensions.PaddingSmall))
            )
        }
    }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 24.dp, bottom = 10.dp)
    ) {

    }
}

@Composable
fun MyBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val tripChilds = listOf("trips", "itinerary", "plan", "album")
    val profileChilds = listOf("profile", "notifications", "preferences", "aboutUs", "termsAndConditions")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomNavItem(
                selected = currentRoute == "home",
                icon = Icons.Default.Home,
                label = stringResource(R.string.bottom_menu_home),
                onClick = { if (currentRoute != "home") navController.navigate("home") }
            )

            // Item 2: Trips
            CustomNavItem(
                selected = currentRoute in tripChilds,
                icon = Icons.Default.Luggage,
                label = stringResource(R.string.bottom_menu_trips),
                onClick = { if (currentRoute != "trips") navController.navigate("trips") }
            )

            // Item 3: Profile/Settings
            CustomNavItem(
                selected = currentRoute in profileChilds,
                icon = Icons.Default.Settings,
                label = stringResource(R.string.preferences_text),
                onClick = { if (currentRoute != "profile") navController.navigate("profile") }
            )
        }
    }
}

@Composable
private fun CustomNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (selected) 1.2f else 1.0f, label = "scale")
    val color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.6f),
        label = "color"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale), // Aplica la escala
            tint = color
        )
        AnimatedVisibility(visible = selected) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun TripCard(place: String, dateIn: Date, dateOut: Date, showNextTitle: Boolean = true, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    val remainingDays = TimeUnit.MILLISECONDS.toDays(dateIn.time - System.currentTimeMillis())

    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val dateInString = dateFormat.format(dateIn)
    val dateOutString = dateFormat.format(dateOut)

    Card(
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showNextTitle) {
                    Text(
                        text = "Tu próximo destino",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.FlightTakeoff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = place,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "$dateInString - $dateOutString • Faltan $remainingDays días",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
            )
        }
    }
}

sealed class WideOptionAction {
    object Arrow : WideOptionAction()
    object None : WideOptionAction()
    data class Toggle(val checked: Boolean, val onCheckedChange: (Boolean) -> Unit) : WideOptionAction()
    data class Menu(
        val currentSelection: String,
        val options: List<String>,
        val isExpanded: Boolean,
        val onDismiss: () -> Unit,
        val onOptionSelected: (String) -> Unit
    ) : WideOptionAction()
}

@Composable
fun WideOption(
    ico: ImageVector,
    text: String,
    secondaryText: String = "",
    rounded: Boolean = true,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit,
    action: WideOptionAction = WideOptionAction.Arrow,
    modifier: Modifier = Modifier
) {
    val shape = if (rounded) RoundedCornerShape(12.dp) else RectangleShape

    Surface(
        onClick = onClick,
        color = color,
        shape = shape,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(ico, contentDescription = null, tint = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                if (secondaryText.isNotEmpty()) {
                    Text(secondaryText, style = MaterialTheme.typography.bodySmall)
                }
            }

            when (action) {
                is WideOptionAction.Arrow -> {
                    Icon(Icons.Filled.ArrowCircleRight, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.scale(1.2f))
                }
                is WideOptionAction.Toggle -> {
                    Switch(
                        checked = action.checked,
                        onCheckedChange = action.onCheckedChange,
                        modifier = Modifier.scale(0.9f)
                    )
                }
                // ... dentro del when(action) de WideOption ...
                is WideOptionAction.Menu -> {
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onClick() } // Asegura que abra al pulsar
                        ) {
                            Text(
                                text = action.currentSelection,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(Icons.Filled.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                        }

                        DropdownMenu(
                            expanded = action.isExpanded,
                            onDismissRequest = { action.onDismiss() }
                        ) {
                                action.options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        action.onOptionSelected(option)
                                        action.onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
                is WideOptionAction.None -> { /* No dibuja nada */ }
            }
        }
    }
}

@Composable
fun PopUp(show: Boolean, title: String, text: String, acceptText: String, cancelText: String, onAccept: () -> Unit, onDismiss: () -> Unit, ) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                TextButton (onClick = onAccept) { Text(text = acceptText) }
            },
            dismissButton = {
                TextButton (onClick = onDismiss) { Text(text = cancelText) }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyTopBar() {
    MyTopBar("Test")
}

@Preview(showBackground = true)
@Composable
fun PreviewMyTopBarBackbutton() {
    MyTopBar("Test", onBackClick = {})
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
fun PreviewLogOutPopUp() {
    PopUp (show = true, title = "Title", text = "Text", acceptText = "Accept", cancelText = "Cancel", onAccept = {}, onDismiss = {})
}

@Preview
@Composable
fun PreviewWideOptionSquaredSecondary() {
    WideOption(Icons.Filled.Settings, "Configuració", secondaryText = "Test", rounded = false, onClick = {})
}