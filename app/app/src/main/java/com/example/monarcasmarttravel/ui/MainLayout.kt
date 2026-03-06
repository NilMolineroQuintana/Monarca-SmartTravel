package com.example.monarcasmarttravel.ui

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.Trip
import java.text.SimpleDateFormat
import java.util.Locale

/** Dimensions globals reutilitzables per mantenir la consistència d'espaiat a tota l'app. */
object AppDimensions {
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 24.dp
}

/**
 * Model d'una acció del menú de la barra superior.
 *
 * @param title Text visible al menú desplegable.
 * @param onClick Acció a executar en seleccionar l'opció.
 */
data class TopBarAction(
    val title: String,
    val onClick: () -> Unit
)

/**
 * Barra superior de l'aplicació.
 *
 * Mostra el títol de la pantalla, opcionalment un botó de retrocés i un menú de tres punts
 * amb les accions indicades.
 *
 * @param title Text del títol. Si és buit, no es mostra.
 * @param menuItems Llista d'accions del menú desplegable. Si és buida, no es mostra el menú.
 * @param onBackClick Acció del botó de retrocés. Si és null, no es mostra el botó.
 */
@Composable
fun MyTopBar(
    title: String = "",
    menuItems: List<TopBarAction> = emptyList(),
    onBackClick: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = if (title.isNotEmpty()) 60.dp else 40.dp,
                bottom = AppDimensions.PaddingMedium
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Enrere",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = if (onBackClick == null) AppDimensions.PaddingMedium else AppDimensions.PaddingSmall)
                )
            }
        }

        // Menú de tres punts, visible només si hi ha accions definides
        if (menuItems.isNotEmpty()) {
            Box(modifier = Modifier.padding(end = AppDimensions.PaddingSmall)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.title) },
                            onClick = {
                                expanded = false
                                item.onClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Barra de navegació inferior de l'aplicació.
 *
 * Conté tres elements de navegació: Inici, Viatges i Preferències. Ressalta l'element
 * corresponent a la ruta activa i escala lleument la icona seleccionada.
 *
 * Les rutes fill de cada secció es mapegen al seu element de navegació per mantenir
 * la selecció activa en navegar a pantalles internes.
 *
 * @param navController Controlador de navegació per detectar la ruta actual i navegar.
 */
@Composable
fun MyBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isDark = isSystemInDarkTheme()

    // Rutes que pertanyen a la secció de viatges
    val tripChilds = listOf("trips", "itinerary", "plan", "album")

    // Rutes que pertanyen a la secció de perfil/preferències
    val profileChilds = listOf("profile", "notifications", "preferences", "aboutUs", "termsAndConditions")

    val barColor = if (isDark) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        color = barColor,
        tonalElevation = if (isDark) 16.dp else 8.dp,
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
            CustomNavItem(
                selected = currentRoute in tripChilds,
                icon = Icons.Default.Luggage,
                label = stringResource(R.string.bottom_menu_trips),
                onClick = { if (currentRoute != "trips") navController.navigate("trips") }
            )
            CustomNavItem(
                selected = currentRoute in profileChilds,
                icon = Icons.Default.Settings,
                label = stringResource(R.string.preferences_text),
                onClick = { if (currentRoute != "profile") navController.navigate("profile") }
            )
        }
    }
}

/**
 * Element individual de la barra de navegació inferior.
 *
 * Anima la mida de la icona i el seu color en funció de si és l'element seleccionat.
 * L'etiqueta de text només s'mostra quan l'element és actiu.
 *
 * @param selected Indica si aquest element és l'actiu.
 * @param icon Icona a mostrar.
 * @param label Text descriptiu de la secció.
 * @param onClick Acció en prémer l'element.
 */
@Composable
private fun CustomNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    // Animació d'escala: l'element actiu és lleugerament més gran
    val scale by animateFloatAsState(targetValue = if (selected) 1.2f else 1.0f, label = "scale")

    // Animació de color: actiu = color primari, inactiu = gris semitransparent
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
            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
            tint = color
        )
        // L'etiqueta apareix i desapareix amb animació segons si l'element és actiu
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

/**
 * Targeta de resum d'un viatge, que mostra el destí, el rang de dates i el temps restant.
 *
 * El text d'estat varia segons si el viatge és futur, imminent, comença avui o ja ha passat.
 * El color del text d'estat es torna vermell si falten 7 dies o menys.
 *
 * @param place Nom del destí del viatge.
 * @param dateIn Data d'inici del viatge.
 * @param dateOut Data de fi del viatge.
 * @param showNextTitle Si és true, mostra "EL TEU PRÒXIM DESTÍ"; si no, "DESTÍ FUTUR".
 * @param onClick Acció en prémer la targeta.
 * @param modifier Modifier opcional per personalitzar el contenidor.
 */
@Composable
fun TripCard(
    trip: Trip,
    showNextTitle: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateIn = trip.dateIn
    val dateOut = trip.dateOut
    val place = trip.destination
    val remainingDays = trip.getDaysUntilStart()

    // Text d'estat dinàmic segons la proximitat del viatge
    val dateStatus = when {
        remainingDays > 1 -> "Falten $remainingDays dies"
        remainingDays == 1L -> "Demà comença el viatge!"
        remainingDays == 0L -> "Comença avui!"
        else -> "Viatge realitzat"
    }

    val hasImage = trip.imageResId != null

    val headerColor = if (hasImage) {
        Color.White
    } else {
        if (showNextTitle) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    }

    val textColor = if (hasImage) Color.White else MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = if (hasImage) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant

    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val dateRange = "${dateFormat.format(dateIn)} - ${dateFormat.format(dateOut)}"

    Card(
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (showNextTitle && !hasImage)
                MaterialTheme.colorScheme.primaryContainer
            else if (!hasImage)
                MaterialTheme.colorScheme.surfaceVariant
            else
                Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            if (trip.imageResId != null) {
                Image(
                    painter = painterResource(id = trip.imageResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(radius = 2.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )
            } else {
                // Fons de color si no hi ha imatge
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (showNextTitle)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = dateStatus,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = headerColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (remainingDays >= 0) Icons.Filled.FlightTakeoff else Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = headerColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = place,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = dateRange,
                            style = MaterialTheme.typography.bodyMedium,
                            color = secondaryTextColor
                        )
                        Text(text = "•", color = secondaryTextColor)
                        // Color vermell si el viatge és en menys de 7 dies
                        Text(
                            text = "Estada de ${trip.getTripDuration()} dies",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (hasImage) {
                                Color.White
                            } else {
                                if (remainingDays in 0..7 && remainingDays >= 0) Color.Red else headerColor
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Sealed class que representa les possibles accions visuals d'una opció ampla ([WideOption]).
 *
 * - [Arrow]: mostra una fletxa a la dreta (navegació).
 * - [None]: no mostra cap acció.
 * - [Toggle]: mostra un interruptor (Switch) per activar/desactivar una opció.
 * - [Menu]: mostra un selector desplegable amb diverses opcions.
 */
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

/**
 * Component d'opció ampla reutilitzable per a llistes de configuració o accions.
 *
 * Mostra una icona, un text principal, un text secundari opcional i una acció a la dreta
 * determinada per [action].
 *
 * @param ico Icona de l'opció.
 * @param text Text principal de l'opció.
 * @param secondaryText Text descriptiu secundari. Opcional.
 * @param rounded Si és true, aplica cantonades arrodonides; si no, és rectangular.
 * @param color Color de fons del component.
 * @param onClick Acció al prémer l'opció.
 * @param action Tipus d'acció visual que es mostra a la dreta (Arrow, Toggle, Menu o None).
 * @param modifier Modifier opcional addicional.
 */
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

            // Renderització de l'acció corresponent a la dreta de l'opció
            when (action) {
                is WideOptionAction.Arrow -> {
                    Icon(
                        Icons.Filled.ArrowCircleRight,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(1.2f)
                    )
                }
                is WideOptionAction.Toggle -> {
                    Switch(
                        checked = action.checked,
                        onCheckedChange = action.onCheckedChange,
                        modifier = Modifier.scale(0.9f)
                    )
                }
                is WideOptionAction.Menu -> {
                    Box {
                        // Selector amb el valor actual i una fletxa desplegable
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onClick() }
                        ) {
                            Text(
                                text = action.currentSelection,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(Icons.Filled.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                        }

                        // Menú desplegable amb les opcions disponibles
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
                is WideOptionAction.None -> { /* No es renderitza cap acció */ }
            }
        }
    }
}

/**
 * Diàleg de confirmació genèric reutilitzable.
 *
 * Mostra un AlertDialog amb títol, text, i botons d'acceptar i cancel·lar.
 * Només es renderitza quan [show] és true.
 *
 * @param show Controla la visibilitat del diàleg.
 * @param title Títol del diàleg.
 * @param text Missatge informatiu del diàleg.
 * @param acceptText Text del botó de confirmació.
 * @param cancelText Text del botó de cancel·lació.
 * @param onAccept Acció en confirmar.
 * @param onDismiss Acció en cancel·lar o tancar el diàleg.
 */
@Composable
fun PopUp(
    show: Boolean,
    title: String,
    text: String,
    acceptText: String = stringResource(R.string.accept),
    cancelText: String = stringResource(R.string.cancel),
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(onClick = onAccept) { Text(text = acceptText) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text(text = cancelText) }
            }
        )
    }
}

/**
 * Obté la versió de l'aplicació instal·lada al dispositiu a partir del PackageManager.
 * Retorna "Desconegut" en cas d'error.
 */
@Composable
fun getAppVersion(): String {
    val context = LocalContext.current
    return try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        packageInfo.versionName ?: "1.0.0"
    } catch (e: Exception) {
        stringResource(R.string.unknown)
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
    WideOption(Icons.Filled.Settings, "Configuració", onClick = {})
}

@Preview
@Composable
fun PreviewWideOptionSecondary() {
    WideOption(Icons.Filled.Settings, "Configuració", secondaryText = "Test", onClick = {})
}

@Preview
@Composable
fun PreviewWideSquaredOption() {
    WideOption(Icons.Filled.Settings, "Configuració", rounded = false, onClick = {})
}

@Preview
@Composable
fun PreviewLogOutPopUp() {
    PopUp(show = true, title = "Title", text = "Text", acceptText = "Accept", cancelText = "Cancel", onAccept = {}, onDismiss = {})
}

@Preview
@Composable
fun PreviewWideOptionSquaredSecondary() {
    WideOption(Icons.Filled.Settings, "Configuració", secondaryText = "Test", rounded = false, onClick = {})
}