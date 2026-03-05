package com.example.monarcasmarttravel.ui.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    val prefe: Preferences = Preferences(usr.userId, false, "Català", true)
    // Mock-up data

    var showLogOutPopUp by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf(prefe.preferredLanguage) }
    var langMenuExpanded by remember { mutableStateOf(false) }

    var darkMode by remember { mutableStateOf(prefe.themeDark) }
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
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        topBar = { MyTopBar(stringResource(id = R.string.preferences_aboutUs_button), onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppDimensions.PaddingMedium,
                    vertical = AppDimensions.PaddingLarge
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingLarge)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_monarca),
                    contentDescription = "Monarca Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .aspectRatio(1f)
                )
                Text(
                    text = "Monarca Smart Travel",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Versió 1.0.0",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
            ) {
                Text(
                    text = "Qui som?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Monarca Smart Travel és la teva aplicació ideal per planificar viatges. Organitza el teu itinerari, controla el teu pressupost i descobreix nous destins de forma fàcil i intel·ligent.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
            ) {
                Text(
                    text = "Equip de Desenvolupament",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "• Nil Molinero\n• Guillem Alcoverro",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TermsAndConditionsScreen(navController: NavController, firstTime: Boolean = true) {
    val context = LocalContext.current

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.preferences_termsAndConditions_button), onBackClick = if (!firstTime) { { navController.popBackStack() } } else null) },
        bottomBar = { if (!firstTime) MyBottomBar(navController) else AcceptOrDeclineBottomBar(
            onAccept = { navController.navigate("login") {
            popUpTo("termsAndConditions?firstTime={firstTime}") { inclusive = true } }
                       },
            onDecline = { (context as? Activity)?.finish() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = AppDimensions.PaddingMedium)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.termsAndConditions_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AcceptOrDeclineBottomBar(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
        ) {
            TextButton(
                onClick = onDecline,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.cancel))
            }

            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.accept))
            }
        }
    }
}

@Preview
@Composable
fun PreviewAcceptOrDeclineBottomBar() {
    AcceptOrDeclineBottomBar(onAccept = {}, onDecline = {})
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

/*
@Preview(showBackground = true)
@Composable
fun TripHistoryPreview() {
    TripHistoryScreen(rememberNavController())
}
*/

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
    AboutUsScreen(rememberNavController())
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