package com.android.inventorytracker.presentation.popup.item_detail.component

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R

@Composable
fun PhotoSelection(
    modifier: Modifier = Modifier,
    image: String?,
    onPickImage: (String?) -> Unit
) {
    val context = LocalContext.current
    var imageUri by rememberSaveable { mutableStateOf(image) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            imageUri = it.toString()
            onPickImage(imageUri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = { launcher.launch(arrayOf("image/*")) }, // OpenDocument expects MIME types array
            modifier = Modifier.fillMaxSize().background(Color.Transparent)
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUri,
                        placeholder = painterResource(R.drawable.outline_add_photo_alternate_24),
                        error = painterResource(R.drawable.outline_add_photo_alternate_24)
                    ),
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop ,
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.outline_add_photo_alternate_24),
                    contentDescription = "Placeholder image",
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
