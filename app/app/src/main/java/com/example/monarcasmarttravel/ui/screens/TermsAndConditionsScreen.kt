package com.example.monarcasmarttravel.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar

/**
 * Pantalla dels termes i condicions de l'aplicació.
 *
 * Pot ser accedida des de dues situacions:
 * - Durant el procés d'inici de sessió ([isLoginScreen] = true): no mostra la barra inferior.
 * - Des de les preferències ([isLoginScreen] = false): mostra la barra de navegació inferior.
 *
 * @param navController Controlador de navegació.
 * @param isLoginScreen Indica si s'accedeix des de la pantalla de login.
 */
@Composable
fun TermsAndConditionsScreen(navController: NavController, isLoginScreen: Boolean = true) {
    Scaffold(
        topBar = {
            MyTopBar(
                stringResource(R.string.preferences_termsAndConditions_button),
                onBackClick = { navController.popBackStack() }
            )
        },
        // La barra inferior només apareix quan no es ve del login
        bottomBar = { if (!isLoginScreen) MyBottomBar(navController) else null }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = AppDimensions.PaddingMedium)
                .verticalScroll(rememberScrollState())
        ) {
            // Text complet dels termes i condicions, desplaçable verticalment
            Text(
                text = stringResource(R.string.termsAndConditions_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}