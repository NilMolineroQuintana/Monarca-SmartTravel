package com.example.monarcasmarttravel.ui.screens.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodels.AuthViewModel
import com.example.monarcasmarttravel.ui.viewmodels.RegisterState

@Composable
fun VerifyEmailScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val registerState by authViewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            authViewModel.resolveStartDestination { dest ->
                navController.navigate(dest) {
                    popUpTo(0) { inclusive = true }
                }
            }
            authViewModel.resetState()
        }
    }

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.verify_email)) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
                .fillMaxSize()
        ) {
            Text(
                stringResource(R.string.verification_sent),
                textAlign = TextAlign.Center
            )
            if (registerState is RegisterState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        authViewModel.isEmailVerified()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = AppDimensions.PaddingSmall),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.register),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun VerifyEmailScreenPreview() {
    VerifyEmailScreen(rememberNavController())
}