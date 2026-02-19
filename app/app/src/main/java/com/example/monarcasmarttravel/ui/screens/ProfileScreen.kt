import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.monarcasmarttravel.ui.MyBottomBar

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        // Aquí va el contenido específico de la Home
        Text("Profile Screen", modifier = Modifier.padding(innerPadding))
    }
}