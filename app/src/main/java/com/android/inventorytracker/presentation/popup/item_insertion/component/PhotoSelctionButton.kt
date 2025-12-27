package com.android.inventorytracker.presentation.popup.item_insertion.component

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

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

    Button(
        modifier = modifier.height(48.dp), // In-adjust ang height para maging uniform sa ibang buttons
        onClick = { launcher.launch(arrayOf("image/*")) },
        shape = RoundedCornerShape(28.dp), // Ginawang rounded para sa modern look
        colors = ButtonDefaults.buttonColors(
            containerColor = Palette.ButtonBeigeBase, // Beige background
            contentColor = Palette.ButtonDarkBrown    // Dark brown text para sa contrast
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )
    }
}