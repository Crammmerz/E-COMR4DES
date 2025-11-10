package com.android.inventorytracker.presentation.shared.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.repository.ItemRepository
import kotlinx.coroutines.launch

class BatchViewModel(private val itemRepository: ItemRepository): ViewModel() {

    fun storeBatch(batch: ItemBatchEntity) {
        viewModelScope.launch {
            val existing = itemRepository.findBatch(batch)
            if (existing==null) {
                itemRepository.insertBatch(batch)
            } else {
                existing.unit += batch.unit
                itemRepository.updateBatch(existing)
            }
        }
    }

    fun deleteBatch(batch: ItemBatchEntity){
        viewModelScope.launch {
            itemRepository.deleteBatch(batch)
        }
    }



    var unit by mutableFloatStateOf(0f)
        private set
    var subUnit by mutableIntStateOf(0)
        private set

    fun unitReset(){
        unit = 0f
        subUnit = 0
    }
    fun onUnitChange(newUnit: Float, threshold: Int) {
        unit = newUnit
        subUnit = (newUnit * threshold).toInt()
    }

    fun onSubUnitChange(newSubUnit: Int, threshold: Int) {
        subUnit = newSubUnit
        unit = newSubUnit.toFloat() / threshold
    }
}