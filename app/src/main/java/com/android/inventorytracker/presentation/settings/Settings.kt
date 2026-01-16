package com.android.inventorytracker.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.settings.component.BackupSettings
import com.android.inventorytracker.presentation.settings.component.Security

@Composable
fun Setting() {
    val backgroundColor = Color(0xFFFEF7ED)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        Security()
        
        Box(modifier = Modifier.padding(horizontal = 40.dp)) {
            BackupSettings()
        }
    }
}
