package com.monarca.smarttravel.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monarca.smarttravel.domain.interfaces.ImageRepository
import com.monarca.smarttravel.domain.model.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val TAG = "ImageViewModel"
    private val _currentTripId = MutableStateFlow<Int?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val images: StateFlow<List<Image>> =_currentTripId
        .filterNotNull()
        .flatMapLatest { tripId -> repository.getImagesByTrip(tripId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadImagesByTrip(tripId: Int) {
        Log.d(TAG, "loadImagesByTrip: observant imatges del viatge id=$tripId")
        _currentTripId.value = tripId
    }

    fun addImage(tripId: Int, path: String) {
        Log.d(TAG, "addImage: intent d'afegir imatge -> tripId=$tripId, path=$path")
        val newImage = Image(tripId = tripId, imagePath = path, dateUploaded = Calendar.getInstance().time)
        viewModelScope.launch {
            repository.addImage(newImage)
        }
    }

    fun deleteImage(image: Image) {
        viewModelScope.launch {
            File(image.imagePath).delete()
            repository.deleteImage(image)
        }

    }
}