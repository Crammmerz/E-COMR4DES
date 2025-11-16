package com.android.inventorytracker.presentation.shared.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.SortBy
import com.android.inventorytracker.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    private val sortBy = MutableStateFlow(SortBy.NAME_ASC)
    // Reactive item list from Room
    val item: Flow<List<ItemEntity>> = itemRepository.getItemList()
    val batch: Flow<List<ItemBatchEntity>> = itemRepository.getBatchList()

    @RequiresApi(Build.VERSION_CODES.O)
    val itemModelList: StateFlow<List<ItemModel>> = combine(item, batch, sortBy) { items, batches, sort ->
        val models = items.map { item ->
            val relatedBatches = batches.filter { it.itemId == item.id }
            ItemModel(item, relatedBatches)
        }
        when (sort) {
            SortBy.NAME_ASC -> models.sortedBy { it.item.name }
            SortBy.NAME_DESC -> models.sortedByDescending { it.item.name }
            SortBy.EXPIRY_SOONEST -> models.sortedBy { it.nearestExpiry }
            SortBy.STOCK_LOW_HIGH -> models.sortedBy { it.totalUnit }
            SortBy.STOCK_HIGH_LOW -> models.sortedByDescending { it.totalUnit }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSort(option: SortBy) {
        sortBy.value = option
    }

    // Insert item safely in background
    fun insertItem(item: ItemEntity){
        viewModelScope.launch {
            itemRepository.insertItem(item)
        }
    }

    fun updateItem(item: ItemEntity){
        viewModelScope.launch {
            itemRepository.updateItem(item)
        }
    }

    fun deleteItem(item: ItemEntity){
        viewModelScope.launch {
            itemRepository.deleteItem(item)
        }
    }
}