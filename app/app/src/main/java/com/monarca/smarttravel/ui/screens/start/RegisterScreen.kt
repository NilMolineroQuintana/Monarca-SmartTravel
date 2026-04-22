package com.monarca.smarttravel.ui.screens.start

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.monarca.smarttravel.R
import com.monarca.smarttravel.ui.AppDimensions
import com.monarca.smarttravel.ui.AppTextField
import com.monarca.smarttravel.ui.DateField
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.viewmodels.AuthViewModel
import com.monarca.smarttravel.ui.viewmodels.RegisterState
import com.monarca.smarttravel.utils.AppError
import com.monarca.smarttravel.utils.validateBirthDate
import com.monarca.smarttravel.utils.validateEmail
import com.monarca.smarttravel.utils.validatePassword
import com.monarca.smarttravel.utils.validatePhone
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.AppDimensions
import com.example.monarcasmarttravel.ui.AppTextField
import com.example.monarcasmarttravel.ui.DateField
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.viewmodels.AuthState
import com.example.monarcasmarttravel.ui.viewmodels.AuthViewModel
import com.example.monarcasmarttravel.ui.viewmodels.RegisterState
import com.example.monarcasmarttravel.utils.AppError
import com.example.monarcasmarttravel.utils.emailPattern
import com.example.monarcasmarttravel.utils.phonePattern
import com.example.monarcasmarttravel.utils.validateBirthDate
import com.example.monarcasmarttravel.utils.validateEmail
import com.example.monarcasmarttravel.utils.validatePassword
import com.example.monarcasmarttravel.utils.validatePhone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "dd/MM/yyyy"

@Composable
fun RegisterScreen(navController: NavController, isCompleting: Boolean) {
    val context = LocalContext.current

    val viewModel: AuthViewModel = hiltViewModel()
    val state by viewModel.registerState.collectAsState()
    val currentStatus = (state as? RegisterState.Error)?.error

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
    val isEmailValid = validateEmail(email)
    val isPhoneValid = validatePhone(phoneNum)
    val passwordMatchesLength = validatePassword(password)
    val equalPasswords = password == confirmPassword
    val isPasswordValid = password.isNotEmpty() && equalPasswords && passwordMatchesLength

    val isFormValid = username.isNotEmpty()
            && validateBirthDate(birthdayText)
            && isEmailValid
            && isPhoneValid
            && isPasswordValid
            && address.isNotEmpty()

    val IsFormValidCompleting = isPhoneValid
            && address.isNotEmpty()
            && validateBirthDate(birthdayText)

    LaunchedEffect(state) {
        when (state) {
            is RegisterState.Success -> {
                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
            }
            is RegisterState.Error -> {
                if (currentStatus == AppError.VERIFICATION_REQUIRED) {
                    navController.navigate("verifyEmail") {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, (state as RegisterState.Error).error.stringRes, Toast.LENGTH_LONG).show()
                }
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { MyTopBar(stringResource(if (!isCompleting) R.string.register else R.string.fill_empty_fields), onBackClick = if (!isCompleting) {
            { navController.popBackStack() }
        } else {
            {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppDimensions.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppDimensions.PaddingMedium)
        ) {
            if (isCompleting) {
                item {
                    Text(stringResource(R.string.missing_fields_explanation))
                }
            }
            item {
                AppTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    label = stringResource(R.string.preferences_username_label),
                    placeholder = "",
                    leadingIcon = Icons.Filled.Person,
                    isError = currentStatus == AppError.EXISTING_USERNAME,
                    errorMessage = stringResource(R.string.error_existing_username)
                )
            }
            if (!isCompleting) {
                item {
                    AppTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = stringResource(R.string.email),
                        placeholder = "",
                        leadingIcon = Icons.Filled.Mail,
                        isError = currentStatus == AppError.EXISTING_EMAIL,
                        errorMessage = stringResource(R.string.error_existing_email)
                    )
                }
            }
            item {
                DateField(
                    value = birthdayText,
                    label = stringResource(R.string.preferences_birthdate_button),
                    onDateSelected = { dateStr ->
                        birthdayText = dateStr
                        birthdayDate = runCatching { sdf.parse(dateStr) }.getOrNull()
                    },
                    showTime = false,
                    modifier = Modifier.fillMaxWidth()
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
            if (!isCompleting) {
                item {
                    val passwordError = when {
                        password.isNotEmpty() && !passwordMatchesLength -> stringResource(AppError.REQUIREMENTS_PASSWORD.stringRes)
                        password.isNotEmpty() && !equalPasswords && confirmPassword.isNotEmpty() -> stringResource(
                            AppError.DIFFERENT_PASSWORDS.stringRes
                        )
                        else -> null
                    }

                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = stringResource(R.string.password),
                        placeholder = stringResource(R.string.password),
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        isError = password.isNotEmpty() && !isPasswordValid,
                        errorMessage = passwordError
                    )
                }
                item {
                    val confirmError = if (confirmPassword.isNotEmpty() && !equalPasswords)
                        stringResource(AppError.DIFFERENT_PASSWORDS.stringRes)
                    else null

                    AppTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = stringResource(R.string.confirm_password),
                        placeholder = stringResource(R.string.confirm_password),
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        isError = confirmPassword.isNotEmpty() && !equalPasswords,
                        errorMessage = confirmError
                    )
                }
            }
            item {
                if (state is RegisterState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            val dateFormatted = birthdayDate?.let { sdf.format(it) } ?: ""
                            viewModel.registerUser(username, dateFormatted, email, phoneNum, address, password, isCompleting)
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
                        enabled = ((!isCompleting && isFormValid) || (isCompleting && IsFormValidCompleting)) && state !is RegisterState.Loading
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
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen(rememberNavController(), false)
}