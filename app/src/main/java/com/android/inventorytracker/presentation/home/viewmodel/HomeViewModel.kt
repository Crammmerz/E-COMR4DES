package com.android.inventorytracker.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import com.android.inventorytracker.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class HomeViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    val itemModelList = itemRepository.observeItemModels()
}