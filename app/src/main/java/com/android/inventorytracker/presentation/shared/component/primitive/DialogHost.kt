package com.android.inventorytracker.presentation.shared.component.primitive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.android.inventorytracker.ui.theme.LightSand

@Composable
fun DialogHost(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    useImePadding: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = modifier
                .background(LightSand, shape = RoundedCornerShape(5.dp))
                .then(if (useImePadding) Modifier.imePadding() else Modifier),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}



