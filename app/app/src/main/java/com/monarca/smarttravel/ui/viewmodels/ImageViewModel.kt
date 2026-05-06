package com.monarca.smarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.monarca.smarttravel.domain.interfaces.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

}