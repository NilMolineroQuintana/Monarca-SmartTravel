package com.example.monarcasmarttravel.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.Image
import com.example.monarcasmarttravel.ui.MyBottomBar
import com.example.monarcasmarttravel.ui.MyTopBar
import com.example.monarcasmarttravel.ui.PopUp
import java.util.Calendar

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen(navController: NavController) {
    // Mock-up data
    val mockData = listOf(
        Image(id = 1, imageId = R.drawable.kyoto, dateUploaded = Calendar.getInstance().time),
        Image(id = 2, imageId = R.drawable.kyoto_2, dateUploaded = Calendar.getInstance().time),
        Image(id = 3, imageId = R.drawable.kyoto_3, dateUploaded = Calendar.getInstance().time),
        Image(id = 4, imageId = R.drawable.kyoto_4, dateUploaded = Calendar.getInstance().time)
    )

    // Mock-up data

    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<Image?>(null) }
    var showPopUp by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { MyTopBar(stringResource(R.string.album), onBackClick = { navController.popBackStack() }) },
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(
                    context,
                    "Funció que s'implementarà més endevant",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(imageVector = Icons.Filled.Upload, contentDescription = null)
            }
        }
    ) { innerPadding ->
        PopUp(
            show = showPopUp,
            title = stringResource(R.string.deleteImage),
            text = stringResource(R.string.popUp_deleteImage_text),
            acceptText = stringResource(R.string.delete),
            onAccept = { showPopUp = false },
            onDismiss = { showPopUp = false }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = innerPadding,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(mockData) { img ->
                Image(
                    painter = painterResource(id = img.imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .combinedClickable(
                            onClick = { selectedImage = img },
                            onLongClick = { showPopUp = true }
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    selectedImage?.let { img ->
        Dialog(
            onDismissRequest = { selectedImage = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = img.imageId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.95f),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}