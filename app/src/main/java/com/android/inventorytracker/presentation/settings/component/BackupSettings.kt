package com.android.inventorytracker.presentation.settings.component

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.settings.viewmodel.BackupViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun BackupSettings(
    viewModel: BackupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lastBackupTime by viewModel.lastBackupTime.collectAsState()
    val backupStatus by viewModel.backupStatus.collectAsState()
    val isBackupAvailable by viewModel.isBackupAvailable.collectAsState()

    // File Pickers for External Storage
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
        onResult = { uri -> uri?.let { viewModel.exportBackup(it) } }
    )

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> uri?.let { viewModel.importBackup(it) } }
    )

    LaunchedEffect(backupStatus) {
        when (backupStatus) {
            is BackupViewModel.BackupStatus.Success -> {
                Toast.makeText(context, (backupStatus as BackupViewModel.BackupStatus.Success).message, Toast.LENGTH_LONG).show()
                viewModel.resetStatus()
            }
            is BackupViewModel.BackupStatus.Error -> {
                Toast.makeText(context, (backupStatus as BackupViewModel.BackupStatus.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetStatus()
            }
            else -> {}
        }
    }

    SecuritySectionCard {
        Text(
            text = "Database Backup & Restore",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Palette.DarkBeigeText
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Last Local Backup: $lastBackupTime",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontSize = 14.sp,
                    color = Palette.DarkBeigeText.copy(alpha = 0.7f)
                )
            )
            if (isBackupAvailable) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = Palette.ButtonBeigeBase.copy(alpha = 0.2f),
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {
                    Text(
                        text = "Available",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Palette.ButtonDarkBrown
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row 1: Internal Backup
        SecurityRowItem(label = "Create Local Backup") {
            SecurityActionButton(
                label = "Backup Now",
                enabled = backupStatus !is BackupViewModel.BackupStatus.Loading,
                onClick = { viewModel.performBackup() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Export to External
        SecurityRowItem(label = "Export to Storage") {
            SecurityActionButton(
                label = "Export File",
                enabled = backupStatus !is BackupViewModel.BackupStatus.Loading && isBackupAvailable,
                onClick = { exportLauncher.launch("inventory_backup_${System.currentTimeMillis()}.db") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 3: Import from External
        SecurityRowItem(label = "Restore from File") {
            SecurityActionButton(
                label = "Import File",
                enabled = false,

//                enabled = backupStatus !is BackupViewModel.BackupStatus.Loading ,
                onClick = { importLauncher.launch(arrayOf("application/octet-stream", "*/*")) }
            )
        }

        if (backupStatus is BackupViewModel.BackupStatus.Loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                color = Palette.ButtonBeigeBase
            )
        }
    }
}
