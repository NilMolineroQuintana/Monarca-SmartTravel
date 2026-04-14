package com.example.monarcasmarttravel.ui.screens.start

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.AppTextField
import com.example.monarcasmarttravel.ui.DateField
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodels.UserViewModel
import com.example.monarcasmarttravel.utils.emailPattern
import com.example.monarcasmarttravel.utils.phonePattern
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "dd/MM/yyyy"

@Composable
fun RegisterScreen(navController: NavController) {
    
    val viewModel: UserViewModel = hiltViewModel()

    var username by rememberSaveable { mutableStateOf("") }
    var birthdayText by remember { mutableStateOf("") }
    var birthdayDate by remember { mutableStateOf<Date?>(null) }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNum by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val sdf = remember { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }
    val isFormValid = username.isNotEmpty() && birthdayDate != null && email.isNotEmpty() && email.matches(emailPattern) && phoneNum.isNotEmpty() && phoneNum.matches(phonePattern) && address.isNotEmpty()

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.register), onBackClick = { navController.popBackStack() } ) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            item {
                AppTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = stringResource(R.string.preferences_username_label),
                    placeholder = "",
                    leadingIcon = Icons.Filled.Person
                )
            }
            item {
                DateField(
                    value = birthdayText,
                    label = stringResource(R.string.preferences_birthdate_button),
                    onDateSelected = { dateStr ->
                        birthdayText = dateStr // Guardamos el String
                        birthdayDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                    },
                    showTime = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.email),
                    placeholder = "",
                    leadingIcon = Icons.Filled.Mail
                )
            }
            item {
                AppTextField(
                    value = phoneNum,
                    onValueChange = { phoneNum = it },
                    label = stringResource(R.string.phone_num),
                    placeholder = "",
                    keyboardType = KeyboardType.Decimal,
                    leadingIcon = Icons.Filled.Phone
                )
            }
            item {
                AppTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = stringResource(R.string.address),
                    placeholder = "",
                    leadingIcon = Icons.Filled.Home
                )
            }
            item {
                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.password),
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                )
            }
            item {
                AppTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = stringResource(R.string.confirm_password),
                    placeholder = stringResource(R.string.confirm_password),
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                )
            }
            item {
                Button(
                    onClick = {
                        val dateFormatted = birthdayDate?.let { sdf.format(it) } ?: ""
                        viewModel.registerUser(username, dateFormatted, email, phoneNum, address, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = AppDimensions.PaddingSmall),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = isFormValid
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
fun RegisterPreview() {
    RegisterScreen(rememberNavController())
}