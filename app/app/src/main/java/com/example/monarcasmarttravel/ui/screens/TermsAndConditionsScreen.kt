package com.example.monarcasmarttravel.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar

@Composable
fun TermsAndConditionsScreen(navController: NavController, firstTime: Boolean = true) {
    val context = LocalContext.current

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.preferences_termsAndConditions_button), onBackClick = if (!firstTime) { { navController.popBackStack() } } else null) },
        bottomBar = { if (!firstTime) MyBottomBar(navController) else AcceptOrDeclineBottomBar(
            onAccept = { navController.navigate("login") {
                popUpTo("termsAndConditions?firstTime={firstTime}") { inclusive = true } }
            },
            onDecline = { (context as? Activity)?.finish() }) }
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
fun AcceptOrDeclineBottomBar(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall)
        ) {
            TextButton(
                onClick = onDecline,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.cancel))
            }

            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.accept))
            }
        }
    }
}

@Preview
@Composable
fun PreviewAcceptOrDeclineBottomBar() {
    AcceptOrDeclineBottomBar(onAccept = {}, onDecline = {})
}