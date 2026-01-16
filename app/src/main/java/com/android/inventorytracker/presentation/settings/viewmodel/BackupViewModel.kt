package com.android.inventorytracker.presentation.settings.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.backup.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupManager: BackupManager
) : ViewModel() {

    private val _lastBackupTime = MutableStateFlow(formatTimestamp(backupManager.getLastBackupTime()))
    val lastBackupTime: StateFlow<String> = _lastBackupTime

    private val _backupStatus = MutableStateFlow<BackupStatus>(BackupStatus.Idle)
    val backupStatus: StateFlow<BackupStatus> = _backupStatus

    private val _isBackupAvailable = MutableStateFlow(backupManager.isBackupAvailable())
    val isBackupAvailable: StateFlow<Boolean> = _isBackupAvailable

    fun performBackup() {
        viewModelScope.launch {
            _backupStatus.value = BackupStatus.Loading
            val result = backupManager.backupDatabase()
            result.fold(
                onSuccess = {
                    _lastBackupTime.value = formatTimestamp(backupManager.getLastBackupTime())
                    _isBackupAvailable.value = backupManager.isBackupAvailable()
                    _backupStatus.value = BackupStatus.Success("Backup created successfully")
                },
                onFailure = {
                    _backupStatus.value = BackupStatus.Error(it.message ?: "Backup failed")
                }
            )
        }
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            _backupStatus.value = BackupStatus.Loading
            val result = backupManager.exportBackup(uri)
            result.fold(
                onSuccess = {
                    _backupStatus.value = BackupStatus.Success("Backup exported successfully")
                },
                onFailure = {
                    _backupStatus.value = BackupStatus.Error(it.message ?: "Export failed")
                }
            )
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            _backupStatus.value = BackupStatus.Loading
            val result = backupManager.importBackup(uri)
            result.fold(
                onSuccess = {
                    _isBackupAvailable.value = backupManager.isBackupAvailable()
                    _backupStatus.value = BackupStatus.Success("Restore successful! Restarting app...")
                    
                    // Small delay to allow the UI/Toast to update before the process kills itself
                    delay(500)
                    // System.exit(0) is often more reliable than Process.killProcess for forced restarts
                    System.exit(0)
                },
                onFailure = {
                    _backupStatus.value = BackupStatus.Error(it.message ?: "Restore failed")
                }
            )
        }
    }

    fun resetStatus() {
        _backupStatus.value = BackupStatus.Idle
    }

    private fun formatTimestamp(timestamp: Long): String {
        if (timestamp == 0L) return "Never"
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    sealed class BackupStatus {
        object Idle : BackupStatus()
        object Loading : BackupStatus()
        data class Success(val message: String) : BackupStatus()
        data class Error(val message: String) : BackupStatus()
    }
}
