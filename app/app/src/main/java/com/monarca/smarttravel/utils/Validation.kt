package com.monarca.smarttravel.utils

val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
val phonePattern = "^\\d{9,15}\$".toRegex()
fun validateBirthDate(date: String): Boolean = date.isNotEmpty()
fun validateEmail(email: String): Boolean = email.isNotEmpty() && email.matches(emailPattern)
fun validatePhone(phoneNum: String) = phoneNum.isNotEmpty() && phoneNum.matches(phonePattern)
fun validatePassword(password: String) = password.isNotEmpty() && password.length >= 8