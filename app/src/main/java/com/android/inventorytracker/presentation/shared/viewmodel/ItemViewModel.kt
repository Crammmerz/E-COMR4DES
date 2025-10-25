package com.android.inventorytracker.presentation.shared.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.database.InventoryDatabase
import com.android.inventorytracker.data.local.entities.ItemEntity
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = InventoryDatabase.Companion.getDatabase(application).itemDao()

    fun isValidItem(name: String, unitThreshold: Int, subUnitThreshold: Int): Boolean {
        return name.isNotBlank() && unitThreshold > 0 && subUnitThreshold > 0
    }

    fun insertItem(newItem: ItemEntity) {
        viewModelScope.launch {
            dao.insert(newItem)
        }
    }
}