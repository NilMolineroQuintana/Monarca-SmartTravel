import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.PopUp
import com.example.monarcasmarttravel.ui.WideOption

@Composable
fun ProfileScreen(navController: NavController) {
    var showLogOutPopUp by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { MyTopBar("Perfil") },
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        PopUp(show = showLogOutPopUp, title = stringResource(R.string.logOut_text), text = stringResource(R.string.logOut_popUp_text), acceptText = stringResource(R.string.popUp_accept), cancelText = stringResource(R.string.popUp_cancel), onAccept = {},onDismiss = { showLogOutPopUp = false })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileInfo(name = "Dummy", email = "dummy@gmail.com")
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                WideOption(ico = Icons.Filled.Notifications, text = stringResource(R.string.preferences_notification_button), onClick = { navController.navigate("notifications") })
                WideOption(ico = Icons.Filled.History, text = stringResource(R.string.travel_historic_button), onClick = {})
                WideOption(ico = Icons.Filled.Settings, text = stringResource(R.string.preferences_text), onClick = { navController.navigate("preferences") })
                WideOption(ico = Icons.Filled.QuestionMark, text = stringResource(R.string.aboutUs_button), onClick = { navController.navigate("aboutUs") })
                WideOption(ico = Icons.AutoMirrored.Filled.Assignment, text = stringResource(R.string.termsAndConditions_button), onClick = { navController.navigate("termsAndConditions") })
                WideOption(ico = Icons.AutoMirrored.Filled.Logout, text = stringResource(R.string.logOut_text), onClick = { showLogOutPopUp = true })
            }
        }
    }
}
@Composable
fun PreferencesScreen(navController: NavController) {
    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.preferences_text), onBackClick = { navController.popBackStack() }) }
        ,bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            WideOption(ico = Icons.Filled.Notifications, text = stringResource(R.string.preferences_notification_button), secondaryText = "Activades", onClick = { })
            WideOption(ico = Icons.Filled.Flag, text = stringResource(R.string.preferences_language_button), secondaryText = stringResource(R.string.language_catalan), onClick = {})
            WideOption(ico = Icons.Filled.FormatPaint,text = stringResource(R.string.preferences_theme_button), secondaryText = stringResource(R.string.theme_light), onClick = {})
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
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                .padding(horizontal = 16.dp, vertical = 24.dp)
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
fun ProfileInfo(name: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.pfp_sample),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.25f)
                .aspectRatio(1f)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .weight(0.75f)
                .padding(start = 16.dp)
        ) {
            Text(stringResource(R.string.profile_page_name_field), style = MaterialTheme.typography.labelSmall)
            Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.profile_page_email_field), style = MaterialTheme.typography.labelSmall)
            Text(text = email, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
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
fun PreferencesPreview() {
    PreferencesScreen(rememberNavController())
}

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