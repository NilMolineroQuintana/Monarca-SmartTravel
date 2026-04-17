package com.example.monarcasmarttravel.ui.screens.start

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.AppTextField
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodels.AuthState
import com.example.monarcasmarttravel.ui.viewmodels.AuthViewModel
import com.example.monarcasmarttravel.utils.emailPattern

@Composable
fun RecoverScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by rememberSaveable { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    val isEmailValid = email.matches(emailPattern)

    /*
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.RecoverEmailSent -> {
                Toast.makeText(context, R.string.email_sent, Toast.LENGTH_LONG).show()
                navController.popBackStack()
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }
    */

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.recover_password), onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxWidth()
        ) {
            item {
                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.email),
                    placeholder = "exemple@correu.com",
                    leadingIcon = Icons.Filled.Email,
                    isError = email.isNotEmpty() && !isEmailValid,
                    errorMessage = if (email.isNotEmpty() && !isEmailValid) stringResource(R.string.invalid_email) else null,
                    enabled = authState !is AuthState.Loading
                )
            }
            item {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { /* viewModel.recoverPassword(email) */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = isEmailValid
                    ) {
                        Text(
                            text = stringResource(R.string.recover),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RecoverScreenPreview() {
    RecoverScreen(rememberNavController())
}