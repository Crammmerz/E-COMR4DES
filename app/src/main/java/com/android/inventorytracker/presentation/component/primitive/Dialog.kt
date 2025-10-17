package com.android.inventorytracker.presentation.component.primitive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogHost(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    showDialog: Boolean = true,
    content: @Composable () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize(0.75f)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                content()
            }
        }
    }
}



