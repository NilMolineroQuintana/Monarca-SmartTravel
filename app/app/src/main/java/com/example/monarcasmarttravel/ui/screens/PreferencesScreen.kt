package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.Preferences
import com.example.monarcasmarttravel.domain.User
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.PopUp
import com.example.monarcasmarttravel.ui.WideOption
import com.example.monarcasmarttravel.ui.WideOptionAction

/**
 * Pantalla de preferències de l'usuari.
 *
 * Permet configurar l'idioma de l'aplicació, el tema (fosc/clar), les notificacions,
 * i accedir a la informació sobre l'app, els termes i condicions i tancar sessió.
 *
 * @param navController Controlador de navegació.
 */
@Composable
fun ProfileScreen(navController: NavController) {

    // Mock-up data
    val usr: User = User("1", "Dummy", "dummy@gmail.com", "123456")
    val prefe: Preferences = Preferences(usr.userId, false, stringResource(R.string.language_catalan), "Dark")

    // Determina si el tema és fosc a partir de les preferències
    val dark: Boolean = prefe.theme == "Dark"

    // Controla la visibilitat del diàleg de confirmació de tancament de sessió
    var showLogOutPopUp by remember { mutableStateOf(false) }

    // Estat del selector d'idioma
    var selectedLanguage by remember { mutableStateOf(prefe.preferredLanguage) }
    var langMenuExpanded by remember { mutableStateOf(false) }

    // Estats dels interruptors de tema i notificacions
    var darkMode by remember { mutableStateOf(dark) }
    var notifications by remember { mutableStateOf(prefe.notificationEnabled) }

    // Color transparent per als botons de les opcions (sense fons propi)
    val buttonsColor = Color.Transparent

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.preferences_text)) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->

        // Diàleg de confirmació per tancar la sessió
        PopUp(
            show = showLogOutPopUp,
            title = stringResource(R.string.preferences_logOut_text),
            text = stringResource(R.string.logOut_popUp_text),
            onAccept = { navController.navigate("login") },
            onDismiss = { showLogOutPopUp = false }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxSize()
        ) {
            // Grup: selecció d'idioma
            item {
                OptionGroup(title = stringResource(R.string.preferences_languageAndRegion)) {
                    WideOption(
                        ico = Icons.Default.Flag,
                        text = stringResource(R.string.preferences_language_button),
                        secondaryText = stringResource(R.string.preferences_language_description),
                        onClick = { langMenuExpanded = true },
                        color = buttonsColor,
                        action = WideOptionAction.Menu(
                            currentSelection = selectedLanguage,
                            options = listOf(
                                stringResource(R.string.language_catalan),
                                stringResource(R.string.language_spanish),
                                stringResource(R.string.language_english)
                            ),
                            isExpanded = langMenuExpanded,
                            onDismiss = { langMenuExpanded = false },
                            onOptionSelected = { selectedLanguage = it }
                        )
                    )
                }
            }

            // Grup: configuració de tema i notificacions
            item {
                OptionGroup(stringResource(R.string.config)) {
                    WideOption(
                        ico = Icons.Filled.FormatPaint,
                        text = stringResource(R.string.preferences_theme_button),
                        secondaryText = stringResource(R.string.preferences_theme_description),
                        rounded = false,
                        color = buttonsColor,
                        action = WideOptionAction.Toggle(darkMode) { darkMode = it },
                        onClick = { darkMode = !darkMode }
                    )
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(
                        ico = Icons.Filled.Notifications,
                        text = stringResource(R.string.preferences_notification_button),
                        secondaryText = stringResource(R.string.preferences_notifications_description),
                        rounded = false,
                        color = buttonsColor,
                        action = WideOptionAction.Toggle(notifications) { notifications = it },
                        onClick = { notifications = !notifications }
                    )
                }
            }

            // Grup: altres opcions (sobre nosaltres, termes, tancar sessió)
            item {
                OptionGroup(title = stringResource(R.string.preferences_other)) {
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(
                        ico = Icons.Filled.QuestionMark,
                        text = stringResource(R.string.preferences_aboutUs_button),
                        secondaryText = stringResource(R.string.preferences_aboutUs_description),
                        rounded = false,
                        color = buttonsColor,
                        onClick = { navController.navigate("aboutUs") }
                    )
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(
                        ico = Icons.AutoMirrored.Filled.Assignment,
                        text = stringResource(R.string.preferences_termsAndConditions_button),
                        secondaryText = stringResource(R.string.preferences_termsAndConditions_description),
                        rounded = false,
                        color = buttonsColor,
                        onClick = { navController.navigate("termsAndConditions") }
                    )
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(
                        ico = Icons.AutoMirrored.Filled.Logout,
                        text = stringResource(R.string.preferences_logOut_text),
                        secondaryText = stringResource(R.string.preferences_logOut_description),
                        rounded = false,
                        color = buttonsColor,
                        onClick = { showLogOutPopUp = true }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(innerPadding.calculateBottomPadding()))
            }
        }
    }
}

/**
 * Component de targeta que agrupa diverses opcions de preferències sota un títol comú.
 *
 * @param title Text del títol del grup, mostrat en majúscules i en color primari.
 * @param modifier Modifier opcional per personalitzar el contenidor extern.
 * @param content Contingut composable que s'inclou dins de la targeta.
 */
@Composable
fun OptionGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )

            HorizontalDivider(thickness = 1.dp)

            Column {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemGroupPreview() {
    OptionGroup(title = "Compte") {
        WideOption(Icons.Default.Person, "Perfil", rounded = false, onClick = { })
        WideOption(Icons.Default.Email, "Cambiar correo", rounded = false, onClick = { })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    ProfileScreen(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsPreview() {
    TermsAndConditionsScreen(rememberNavController())
}

@Preview
@Composable
fun PreviewLogOutPopUp() {
    PopUp(show = true, title = "Title", text = "Text", acceptText = "Accept", cancelText = "Cancel", onAccept = {}, onDismiss = {})
}