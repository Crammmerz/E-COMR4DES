package com.android.inventorytracker.presentation.popup.item_insertion.component

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun PhotoSelectionButton(
    modifier: Modifier = Modifier,
    image: String?,
    onPickImage: (String?) -> Unit
) {
    val context = LocalContext.current
    var imageUri by rememberSaveable { mutableStateOf(image) }
    var text by rememberSaveable { mutableStateOf("Select Image") }


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
    LaunchedEffect(imageUri) {
        onPickImage(imageUri)
        text = if (imageUri != null) "Change Image" else "Select Image"
    }

    Button(modifier = modifier, onClick = { launcher.launch(arrayOf("image/*")) }) {
        Text(
            text = text,
        )
    }
}
