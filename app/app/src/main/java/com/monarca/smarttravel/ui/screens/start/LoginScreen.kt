package com.monarca.smarttravel.ui.screens.start

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.monarca.smarttravel.R
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.AppTextField
import com.monarca.smarttravel.ui.viewmodels.AuthState
import com.monarca.smarttravel.ui.viewmodels.AuthViewModel
import com.monarca.smarttravel.utils.AppError
import com.monarca.smarttravel.utils.emailPattern

/**
 * Pantalla d'inici de sessió de l'aplicació.
 *
 * Mostra el logotip, el nom de l'app i un formulari amb els camps de correu electrònic
 * i contrasenya. L'usuari ha d'acceptar els termes i condicions per poder iniciar sessió.
 *
 * La validació del correu es fa en temps real mitjançant una expressió regular.
 * El botó d'inici de sessió només s'activa quan el formulari és vàlid:
 * correu correcte, contrasenya no buida i termes acceptats.
 *
 * Des d'aquí es pot navegar a [com.monarca.smarttravel.ui.screens.preferences.TermsAndConditionsScreen] per llegir les condicions,
 * i a [HomeScreen] en completar l'inici de sessió correctament.
 *
 * @param navController Controlador de navegació.
 */
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberTerms by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    val isEmailValid = email.matches(emailPattern) || email.isEmpty()
    val isFormValid = email.matches(emailPattern) && password.isNotEmpty() && rememberTerms && authState !is AuthState.Loading

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthState.Error -> {
                when ((authState as AuthState.Error).error) {
                    AppError.MISSING_FIELDS -> navController.navigate("register?isCompleting=true") {
                        popUpTo("login") { inclusive = true }
                    }
                    AppError.VERIFICATION_REQUIRED -> navController.navigate("verifyEmail") {
                        popUpTo("login") { inclusive = true }
                    }
                    else -> Toast.makeText(context, (authState as AuthState.Error).error.stringRes, Toast.LENGTH_LONG).show()
                }
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // logo i títol
            Image(
                painter = painterResource(id = R.drawable.logo_monarca),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.which_adventure),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // formulari
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email),
                placeholder = "exemple@correu.com",
                leadingIcon = Icons.Default.Email,
                isError = !isEmailValid,
                errorMessage = if (!isEmailValid) stringResource(R.string.invalid_email) else null,
                keyboardType = KeyboardType.Email,
                enabled = authState !is AuthState.Loading
            )

            AppTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password),
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardType = KeyboardType.Password,
                modifier = Modifier.padding(top = AppDimensions.PaddingSmall),
                enabled = authState !is AuthState.Loading
            )

            Spacer(modifier = Modifier.height(AppDimensions.PaddingSmall))

            // termes i condicions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = rememberTerms,
                    onCheckedChange = { rememberTerms = it },
                    enabled = authState !is AuthState.Loading
                )

                Text(
                    text = stringResource(R.string.i_agree_with),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(R.string.preferences_termsAndConditions_button).lowercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(enabled = authState !is AuthState.Loading) {
                            navController.navigate("termsAndConditions?isLoginScreen=true")
                        }
                        .padding(4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.no_account))
                Text(
                    text = stringResource(R.string.register),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(enabled = authState !is AuthState.Loading) {
                            navController.navigate("register")
                        }
                        .padding(4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.forgot_password))
                Text(
                    text = stringResource(R.string.recover),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(enabled = authState !is AuthState.Loading) {
                            navController.navigate("recoverPassword")
                        }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // botó de login
            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isFormValid,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}