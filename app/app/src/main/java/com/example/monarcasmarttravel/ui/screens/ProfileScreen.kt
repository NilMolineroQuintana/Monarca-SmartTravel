package com.example.monarcasmarttravel.ui.screens

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.User
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.PopUp
import com.example.monarcasmarttravel.ui.WideOption

@Composable
fun ProfileScreen(navController: NavController) {
    // Mock-up data
    val usr: User = User("1", "Dummy", "dummy@gmail.com", R.drawable.pfp_sample)
    // Mock-up data
    var showLogOutPopUp by remember { mutableStateOf(false) }
    
    val ButtonsColor = Color.Transparent
    Scaffold(
        topBar = { MyTopBar("Preferències") },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        PopUp(show = showLogOutPopUp, title = stringResource(R.string.logOut_text), text = stringResource(R.string.logOut_popUp_text), acceptText = stringResource(R.string.popUp_accept), cancelText = stringResource(R.string.popUp_cancel), onAccept = {},onDismiss = { showLogOutPopUp = false })
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxSize()
        ) {
            item {
                OptionGroup(title = "IDIOMA I REGIÓ") {
                    WideOption(ico = Icons.Filled.Flag, text = stringResource(R.string.preferences_language_button), secondaryText = "Idioma de la interficie", rounded = false, color = ButtonsColor, onClick = { /*...*/ })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.Default.Payments, text = "Moneda", secondaryText = "Moneda a usar al pressupost", rounded = false, color = ButtonsColor, onClick = { /*...*/ })
                }
            }
            item {
                OptionGroup(title = "APARENÇA") {
                    WideOption(Icons.Filled.FormatPaint, stringResource(R.string.preferences_theme_button), secondaryText = "Colors de la interficie", rounded = false, color = ButtonsColor, onClick = { /*...*/ })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(Icons.Default.TextFields, "Mida del text", secondaryText = "Ajuda a l'accessibilitat", rounded = false, color = ButtonsColor, onClick = { /*...*/ })
                }
            }
            item {
                OptionGroup(title = "ALTRES") {
                    WideOption(ico = Icons.Filled.Notifications, text = stringResource(R.string.preferences_notification_button), secondaryText = "Rebre notifiacions rellevants", rounded = false, color = ButtonsColor, onClick = { })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.Filled.QuestionMark, text = stringResource(R.string.aboutUs_button), secondaryText = "Conèix més informació sobre l'app", rounded = false, color = ButtonsColor, onClick = { navController.navigate("aboutUs") })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.AutoMirrored.Filled.Assignment, text = stringResource(R.string.termsAndConditions_button), secondaryText = "Els teus drets i deures", rounded = false, color = ButtonsColor, onClick = { navController.navigate("termsAndConditions") })
                    HorizontalDivider(thickness = 1.dp)
                    WideOption(ico = Icons.AutoMirrored.Filled.Logout, text = stringResource(R.string.logOut_text), secondaryText = "Surt del teu compte de forma segura", rounded = false, color = ButtonsColor, onClick = { showLogOutPopUp = true })
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
        topBar = { MyTopBar(stringResource(id = R.string.aboutUs_button), onBackClick = { navController.popBackStack() }) },
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
fun TermsAndConditionsScreen(navController: NavController) {
    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.termsAndConditions_button), onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) }
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
fun MyRadioButtonGroup(options: List<String>) {
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column {
        options.forEach { text ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { selectedOption = text }
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun OptionGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit // Usamos ColumnScope para que los hijos hereden el comportamiento de columna
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow // Un tono sutil
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary, // Color acentuado para el título
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
    OptionGroup(title = "Cuenta") {
        WideOption(Icons.Default.Person, "Perfil", rounded = false,onClick = { /*...*/ })
        WideOption(Icons.Default.Email, "Cambiar correo", rounded = false,onClick = { /*...*/ })
    }
}

@Preview(showBackground = true)
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