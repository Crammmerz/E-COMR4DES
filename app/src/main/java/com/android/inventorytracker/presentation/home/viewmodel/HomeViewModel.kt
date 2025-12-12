package com.android.inventorytracker.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
@OptIn(FlowPreview::class)
class HomeViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    val itemModelList = itemRepository.observeItemModels()

    val expiryItems: Flow<List<ItemModel>> = itemModelList.map { list ->
        list.filter { it.isExpiringSoon }
            .sortedBy { it.nearestExpiryDate }
    }

    val stockItems: Flow<List<ItemModel>> = itemModelList.map { list ->
        list.filter { it.isLowStock }
            .sortedBy { it.totalUnit() / it.item.unitThreshold * 0.20f }
    }
}