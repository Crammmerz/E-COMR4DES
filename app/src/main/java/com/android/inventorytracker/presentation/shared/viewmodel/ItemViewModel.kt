package com.android.inventorytracker.presentation.shared.viewmodel

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val _sortBy = MutableStateFlow(SortBy.NAME_ASC)
    val sortBy: StateFlow<SortBy> = _sortBy

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val itemModelList: StateFlow<List<ItemModel>> = combine(
        itemRepository.observeItemModels(),
        _sortBy,
        _query.debounce(250)
    ) { models, sort, q ->
        val filtered = if (q.isBlank()) models else models.filter {
            it.item.name.contains(q, true) || it.item.description?.contains(q, true) == true
        }
        applySort(filtered, sort)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun applySort(models: List<ItemModel>, sort: SortBy) =
        when (sort) {
            SortBy.NAME_ASC -> models.sortedBy { it.item.name }
            SortBy.NAME_DESC -> models.sortedByDescending { it.item.name }
            SortBy.EXPIRY_SOONEST -> models.sortedWith(compareBy(nullsLast()) { it.nearestExpiryDate })
            SortBy.STOCK_LOW_HIGH -> models.sortedBy { it.totalUnit }
            SortBy.STOCK_HIGH_LOW -> models.sortedByDescending { it.totalUnit }
        }

    fun setSearchQuery(q: String) { _query.value = q }
    fun setSort(option: SortBy) { _sortBy.value = option }

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
