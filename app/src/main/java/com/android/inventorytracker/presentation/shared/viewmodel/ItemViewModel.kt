package com.android.inventorytracker.presentation.shared.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.SortBy
import com.android.inventorytracker.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    var pendingActionItem by mutableStateOf<ItemModel?>(null)
        private set
    var pendingActionType by mutableStateOf<String?>(null)
        private set

    fun triggerNotificationAction(model: ItemModel, type: String) {
        pendingActionItem = model
        pendingActionType = type
    }

    fun clearPendingAction() {
        pendingActionItem = null
        pendingActionType = null
    }

    private val _sortBy = MutableStateFlow(SortBy.NAME_ASC)
    val sortBy: StateFlow<SortBy> = _sortBy

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _persistentItems = MutableStateFlow<Set<Int>>(emptySet())
    val persistentItems: StateFlow<Set<Int>> = _persistentItems

    val itemModelList: StateFlow<List<ItemModel>> = combine(
        itemRepository.observeItemModels(),
        _sortBy,
        _query.debounce(250),
        _persistentItems
    ) { models, sort, q, persist ->
        val filtered = if (q.isBlank()) {
            models
        } else {
            models.filter { model ->
                model.item.id in persist || (
                        model.item.name.contains(q, ignoreCase = true) ||
                                model.item.description?.contains(q, ignoreCase = true) == true
                        )
            }
        }
        applySort(filtered, sort, persist)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun applySort(models: List<ItemModel>, sort: SortBy, persist: Set<Int>) =
        when (sort) {
            SortBy.NAME_ASC -> models.sortedBy { it.item.name }
            SortBy.NAME_DESC -> models.sortedByDescending { it.item.name }
            SortBy.EXPIRY_ASCENDING -> models.sortedWith(compareBy(nullsLast()) { it.nearestExpiryDate })
            SortBy.STOCK_LOW -> models.sortedBy { (it.totalUnit() / it.item.unitThreshold).toFloat() }.filter { it.isLowStock || it.item.id in persist }
            SortBy.STOCK_LOW_HIGH -> models.sortedBy { it.totalUnit() }
            SortBy.STOCK_HIGH_LOW -> models.sortedByDescending { it.totalUnit() }
        }

    fun setSearchQuery(q: String) { _query.value = q }
    fun setSort(option: SortBy) { _sortBy.value = option }

    fun reset() {
        _persistentItems.value = emptySet()
        _query.value = ""
        _sortBy.value = SortBy.NAME_ASC
    }

    fun togglePersistence(itemId: Int, persistent: Boolean) {
        _persistentItems.update { current ->
            if (persistent) current + itemId else current - itemId
        }
    }

    fun insertItem(item: ItemEntity) {
        viewModelScope.launch { itemRepository.insertItem(item) }
    }
    fun updateItem(item: ItemEntity) {
        viewModelScope.launch { itemRepository.updateItem(item) }
    }
    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch { itemRepository.deleteItem(item) }
    }
}