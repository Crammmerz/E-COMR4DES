package com.android.inventorytracker.presentation.shared.viewmodel

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.SortBy
import com.android.inventorytracker.data.repository.ItemRepository
import com.android.inventorytracker.services.notification.NotificationChannels
import com.android.inventorytracker.services.notification.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    val item: Flow<List<ItemEntity>> = itemRepository.getItemList()
    val batch: Flow<List<ItemBatchEntity>> = itemRepository.getBatchList()
    private val sortBy = MutableStateFlow(SortBy.NAME_ASC)
    private val searchQuery = MutableStateFlow("")

    @RequiresApi(Build.VERSION_CODES.O)
    val itemModelList: StateFlow<List<ItemModel>> = combine(item, batch, sortBy, searchQuery) { items, batches, sort, query ->
        val batchMap = batches.groupBy { it.itemId }
        val models = items.map { item -> ItemModel(item, batchMap[item.id].orEmpty()) }
        applySort(applySearch(models, query), sort)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun applySearch(models: List<ItemModel>, query: String) =
        if (query.isBlank()) models
        else models.filter { it.item.name.contains(query, true) || it.item.description?.contains(query, true) == true }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun applySort(models: List<ItemModel>, sort: SortBy) =
        when (sort) {
            SortBy.NAME_ASC -> models.sortedBy { it.item.name }
            SortBy.NAME_DESC -> models.sortedByDescending { it.item.name }
            SortBy.EXPIRY_SOONEST -> models.sortedBy { it.nearestExpiry }
            SortBy.STOCK_LOW_HIGH -> models.sortedBy { it.totalUnit }
            SortBy.STOCK_HIGH_LOW -> models.sortedByDescending { it.totalUnit }
        }
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
    fun setSort(option: SortBy) {
        sortBy.value = option
    }

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

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notifyOutOfStock(itemName: String) {
        val notification = notificationHelper.buildNotification(
            NotificationChannels.OUT_OF_STOCK,
            "Out of Stock",
            "$itemName is completely out of stock"
        )
        notificationHelper.postNotification(1001, notification)
    }
}