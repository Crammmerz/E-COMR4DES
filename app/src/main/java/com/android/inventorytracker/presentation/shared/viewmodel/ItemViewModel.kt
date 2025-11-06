package com.android.inventorytracker.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {


    // Reactive item list from Room
    val item: Flow<List<ItemEntity>> = itemRepository.getItemList()
    val batch: Flow<List<ItemBatchEntity>> = itemRepository.getBatchList()

    val itemModelList: StateFlow<List<ItemModel>> = combine(
        item,
        batch
    ) { items, batches ->
        items.map { item ->
            val relatedBatches = batches.filter { it.itemId == item.id }
            ItemModel(item, relatedBatches)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


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