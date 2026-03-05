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

@Composable
fun ProfileScreen(navController: NavController) {
    // Mock-up data
    val usr: User = User("1", "Dummy", "dummy@gmail.com", "123456")
    val prefe: Preferences = Preferences(usr.userId, false, stringResource(R.string.language_catalan), "Dark")
    val dark: Boolean = when (prefe.theme) {
        "Dark" -> true
        else -> false
    }
    // Mock-up data

    var showLogOutPopUp by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf(prefe.preferredLanguage) }
    var langMenuExpanded by remember { mutableStateOf(false) }

    var darkMode by remember { mutableStateOf(dark) }
    var notifications by remember { mutableStateOf(prefe.notificationEnabled) }

    val ButtonsColor = Color.Transparent
    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.preferences_text)) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        PopUp(show = showLogOutPopUp, title = stringResource(R.string.preferences_logOut_text), text = stringResource(R.string.logOut_popUp_text), onAccept = { navController.navigate("login") },onDismiss = { showLogOutPopUp = false })
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxSize()
        ) {
            item {
                OptionGroup(title = stringResource(R.string.preferences_languageAndRegion)) {
                    WideOption(
                        ico = Icons.Default.Flag,
                        text = stringResource(R.string.preferences_language_button),
                        secondaryText = stringResource(R.string.preferences_language_description),
                        onClick = { langMenuExpanded = true },
                        color = ButtonsColor,
                        action = WideOptionAction.Menu(
                            currentSelection = selectedLanguage,
                            options = listOf(stringResource(R.string.language_catalan), stringResource(R.string.language_spanish), stringResource(R.string.language_english)),
                            isExpanded = langMenuExpanded,
                            onDismiss = { langMenuExpanded = false },
                            onOptionSelected = { selectedLanguage = it }
                        )
                    )
                }
            }
            item {
                OptionGroup(stringResource(R.string.config)) {
                    WideOption(Icons.Filled.FormatPaint, stringResource(R.string.preferences_theme_button), secondaryText = stringResource(R.string.preferences_theme_description), rounded = false, color = ButtonsColor, action = WideOptionAction.Toggle(darkMode) { darkMode = it}, onClick = { darkMode = !darkMode })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.Filled.Notifications, text = stringResource(R.string.preferences_notification_button), secondaryText = stringResource(R.string.preferences_notifications_description), rounded = false, color = ButtonsColor, action = WideOptionAction.Toggle (notifications) { notifications = it } ,onClick = { notifications = !notifications })
                }
            }
            item {
                OptionGroup(title = stringResource(R.string.preferences_other)) {
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.Filled.QuestionMark, text = stringResource(R.string.preferences_aboutUs_button), secondaryText = stringResource(R.string.preferences_aboutUs_description), rounded = false, color = ButtonsColor, onClick = { navController.navigate("aboutUs") })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.AutoMirrored.Filled.Assignment, text = stringResource(R.string.preferences_termsAndConditions_button), secondaryText = stringResource(R.string.preferences_termsAndConditions_description), rounded = false, color = ButtonsColor, onClick = { navController.navigate("termsAndConditions") })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.AutoMirrored.Filled.Logout, text = stringResource(R.string.preferences_logOut_text), secondaryText = stringResource(R.string.preferences_logOut_description), rounded = false, color = ButtonsColor, onClick = { showLogOutPopUp = true })
                }
            }
            item {
                Spacer(modifier = Modifier.size(innerPadding.calculateBottomPadding()))
            }
        }
    }
}

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
        WideOption(Icons.Default.Person, "Perfil", rounded = false,onClick = { /*...*/ })
        WideOption(Icons.Default.Email, "Cambiar correo", rounded = false,onClick = { /*...*/ })
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
    PopUp (show = true, title = "Title", text = "Text", acceptText = "Accept", cancelText = "Cancel", onAccept = {}, onDismiss = {})
}