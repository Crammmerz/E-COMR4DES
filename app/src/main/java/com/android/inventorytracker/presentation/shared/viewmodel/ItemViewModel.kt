package com.android.inventorytracker.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {


    // Reactive item list from Room
    val itemList: Flow<List<ItemEntity>> = itemRepository.getItemList()
    val itemBatchList: Flow<List<ItemBatchEntity>> = itemRepository.getBatchList()

    // Insert item safely in background
    fun insertItem(item: ItemEntity){
        viewModelScope.launch {
            itemRepository.addNewItem(item)
        }
    }

    fun updateItem(item: ItemEntity){
        viewModelScope.launch {
            itemRepository.updateItem(item)
        }
    }
}