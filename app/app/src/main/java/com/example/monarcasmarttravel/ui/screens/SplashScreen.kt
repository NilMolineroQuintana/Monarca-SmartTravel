package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.getAppVersion
import kotlinx.coroutines.delay

/**
 * Pantalla de càrrega inicial de l'aplicació (splash screen).
 *
 * Mostra el logotip, el nom de l'app i una barra de progrés animada que avança de 0 a 1
 * en increments de 0,01 cada 15 ms (~1,5 segons en total). Quan la barra arriba al final,
 * s'invoca [onTimeout] per navegar a la pantalla de login.
 *
 * La versió de l'app es mostra a la cantonada inferior dreta.
 *
 * @param onTimeout Acció a executar quan la càrrega finalitza.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    // Progrés de la barra de càrrega, entre 0.0 i 1.0
    var progress by remember { mutableStateOf(0f) }

    // Efecte llançat una sola vegada: incrementa el progrés fins a 1 i crida onTimeout
    LaunchedEffect(Unit) {
        repeat(100) {
            progress += 0.01f
            delay(15)
        }
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_monarca),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Barra de progrés animada amb cantonades arrodonides
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(200.dp)
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )
        }

        // Versió de l'app a la cantonada inferior dreta
        Text(
            text = "V.${getAppVersion()}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onTimeout = {})
}