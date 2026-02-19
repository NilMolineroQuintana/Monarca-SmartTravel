import androidx.compose.animation.AnimatedContent
import com.example.monarcasmarttravel.R
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monarcasmarttravel.ui.MyBottomBar

@Composable
fun ProfileScreen(navController: NavController) {
    var shouldShowMain by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        // AnimatedContent observa los cambios en su parámetro (targetState)
        AnimatedContent(
            targetState = shouldShowMain,
            modifier = Modifier.padding(innerPadding),
            label = "profile_transition"
        ) { isMainVisible -> // El parámetro nos dice qué estado dibujar ahora
            if (isMainVisible) {
                MainProfileContent(onSettingsClicked = { shouldShowMain = false })
            } else {
                PreferencesContent(onBackClicked = { shouldShowMain = true })
            }
        }
    }
}
@Composable
fun MainProfileContent(onSettingsClicked: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileInfo(name = "Dummy", email = "hola@gmail.com")
        WideOption(ico = Icons.Filled.Settings, text = "Preferències", onClick = onSettingsClicked)
    }
}

@Composable
fun PreferencesContent(onBackClicked: () -> Unit = {}) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Preferències", style = MaterialTheme.typography.headlineMedium)
    }
}
@Composable
fun ProfileInfo(name: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Margen externo para que respire
        verticalAlignment = Alignment.CenterVertically
    ) {
        // La imagen ocupará el 25% del ancho disponible (adaptable)
        Image(
            painter = painterResource(id = R.drawable.pfp_sample),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.25f) // <--- Ocupa una proporción
                .aspectRatio(1f) // <--- Mantiene el cuadrado pase lo que pase
                .clip(CircleShape) // Opcional: queda muy bien en perfiles
        )

        // El texto ocupará el resto (75%)
        Column(
            modifier = Modifier
                .weight(0.75f)
                .padding(start = 16.dp) // Espacio entre imagen y texto
        ) {
            // Usamos estilos del MaterialTheme para que el tamaño de fuente sea dinámico
            Text("Nom:", style = MaterialTheme.typography.labelSmall)
            Text("Nil", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Correu:", style = MaterialTheme.typography.labelSmall)
            Text("hola@gmail.com", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WideOption(ico: ImageVector, text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant, // Color más suave que el Primary
        shape = RoundedCornerShape(12.dp), // Esquinas redondeadas se adaptan mejor visualmente
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Separación entre opciones
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp) // Padding interno del botón
        ) {
            Icon(
                imageVector = ico,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
fun PreviewProfileInfo() {
    ProfileInfo("Nil", "hola@gmail.com")
}

@Preview
@Composable
fun PreviewWideOption() {
    WideOption(Icons.Filled.Settings, "Configuració", onClick = {})
}