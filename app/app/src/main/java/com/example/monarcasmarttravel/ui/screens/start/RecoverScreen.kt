package com.example.monarcasmarttravel.ui.screens.start

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.AppTextField
import com.example.monarcasmarttravel.ui.MyTopBar

@Composable
fun RecoverScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.recover_password), onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
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
                    placeholder = "",
                    leadingIcon = Icons.Filled.Email
                )
            }
            item {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
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

@Composable
@Preview(showBackground = true)
fun RecoverScreenPreview() {
    RecoverScreen(rememberNavController())
}