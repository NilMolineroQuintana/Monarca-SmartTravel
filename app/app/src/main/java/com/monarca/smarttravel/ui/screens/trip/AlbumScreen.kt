package com.monarca.smarttravel.ui.screens.trip

import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.monarca.smarttravel.R
import com.monarca.smarttravel.domain.model.Image
import com.monarca.smarttravel.ui.MyBottomBar
import com.monarca.smarttravel.ui.MyTopBar
import com.monarca.smarttravel.ui.PopUp
import com.monarca.smarttravel.ui.viewmodels.ImageViewModel
import com.monarca.smarttravel.utils.saveImageToInternalStorage
import java.io.File
import java.util.Calendar
import kotlin.let

/**
 * Pantalla de l'àlbum fotogràfic associat a un viatge concret.
 *
 * Mostra una graella d'imatges del viatge. Amb un clic es visualitza la imatge
 * a pantalla completa, i amb un clic llarg apareix un diàleg de confirmació per eliminar-la.
 *
 * @param navController Controlador de navegació per moure's entre pantalles.
 * @param tripId Identificador del viatge del qual es mostren les fotos.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen(navController: NavController, tripId: Int) {

    val imageViewModel: ImageViewModel = hiltViewModel()

    val images by imageViewModel.images.collectAsStateWithLifecycle()

    LaunchedEffect(tripId) {
        imageViewModel.loadImagesByTrip(tripId)
    }

    val context = LocalContext.current

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            val path = saveImageToInternalStorage(context, uri)
            if (path != null) imageViewModel.addImage(tripId, path)
        }
    }

    // ── Càmera ──────────────────────────────────────────────────────────────────
    var cameraImageFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageFile?.absolutePath?.let { path ->
                imageViewModel.addImage(tripId, path)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File(
                context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
                "IMG_${System.currentTimeMillis()}.jpg"
            )
            cameraImageFile = file
            val uri = androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            cameraLauncher.launch(uri)
        }
    }

    // Imatge seleccionada per mostrar en el visor a pantalla completa
    var selectedImage by remember { mutableStateOf<Image?>(null) }
    var imageToDelete by remember { mutableStateOf<Image?>(null) }

    // Controla la visibilitat del diàleg de confirmació d'eliminació
    var showPopUp by remember { mutableStateOf(false) }


    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.album), onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botó petit: càmera
                SmallFloatingActionButton(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null
                    )
                }
                // Botó principal: galeria
                FloatingActionButton(onClick = {
                    imageLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Icon(imageVector = Icons.Filled.Upload, contentDescription = null)
                }
            }
        },
    ) { innerPadding ->

        // Diàleg de confirmació per eliminar una imatge
        PopUp(
            show = showPopUp,
            title = stringResource(R.string.deleteImage),
            text = stringResource(R.string.popUp_deleteImage_text),
            acceptText = stringResource(R.string.delete),
            onAccept = {
                imageToDelete?.let { imageViewModel.deleteImage(it) }
                showPopUp = false
                imageToDelete = null
            },
            onDismiss = {
                showPopUp = false
                imageToDelete = null
            }
        )

        // Graella de 3 columnes amb les imatges del viatge
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = innerPadding,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(images, key = { it.id }) { img ->
                AsyncImage(
                    model = File(img.imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .combinedClickable(
                            onClick = { selectedImage = img },
                            onLongClick = {
                                imageToDelete = img
                                showPopUp = true
                            }
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    // Visor d'imatge a pantalla completa amb zoom i pan
    selectedImage?.let { img ->
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)
            offset += panChange * scale
        }

        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = File(img.imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.95f)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        .transformable(state = transformableState),
                    contentScale = ContentScale.Fit
                )
                // Botó de tancament a la cantonada superior dreta
                IconButton(
                    onClick = {
                        selectedImage = null
                        scale = 1f
                        offset = Offset.Zero
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}