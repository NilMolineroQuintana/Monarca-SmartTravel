import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.monarcasmarttravel.ui.WideOption

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileInfo(name = "Dummy", email = "hola@gmail.com")
            Column() {
                WideOption(ico = Icons.Filled.History, text = stringResource(R.string.travel_historic_button), onClick = {})
                WideOption(ico = Icons.Filled.Settings, text = stringResource(R.string.preferences_button), onClick = { navController.navigate("preferences") })
                WideOption(ico = Icons.AutoMirrored.Filled.Logout, text = stringResource(R.string.sign_out_button), onClick = {})
            }
        }
    }
}
@Composable
fun PreferencesScreen(navController: NavController) {
    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            WideOption(
                ico = Icons.Filled.Flag,
                text = stringResource(R.string.preferences_language_button),
                secondaryText = stringResource(R.string.language_catalan),
                onClick = {})
            WideOption(
                ico = Icons.Filled.FormatPaint,
                text = stringResource(R.string.preferences_theme_button),
                secondaryText = stringResource(R.string.theme_light),
                onClick = {})
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

@Preview()
@Composable
fun PreviewProfileInfo() {
    ProfileInfo("Nil", "hola@gmail.com")
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    ProfileScreen(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PreferencesPreview() {
    PreferencesScreen(rememberNavController())
}
