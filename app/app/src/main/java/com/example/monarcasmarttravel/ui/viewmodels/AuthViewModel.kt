package com.example.monarcasmarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.monarcasmarttravel.domain.interfaces.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

}