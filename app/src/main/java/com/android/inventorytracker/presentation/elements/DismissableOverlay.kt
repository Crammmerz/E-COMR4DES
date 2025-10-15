package com.android.inventorytracker.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable // Dark Background Used for POP UPS when pressed triggers an Exit response to the popup
fun DismissibleOverlay(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.first().position
                        // Trigger dismiss only if the click is outside children
                        if (event.changes.first().pressed) {
                            onDismiss()
                            break
                        }
                    }
                }
            }
    ) {
        content()
    }
}