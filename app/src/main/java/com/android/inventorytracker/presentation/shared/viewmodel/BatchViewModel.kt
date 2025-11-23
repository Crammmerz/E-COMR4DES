package com.android.inventorytracker.presentation.shared.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BatchViewModel @Inject constructor(
    private val itemRepository: ItemRepository
): ViewModel() {

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
    fun storeBatch(batch: ItemBatchEntity) {
        viewModelScope.launch {
            val existing = itemRepository.findBatch(batch.itemId, batch.expiryDate)
            if (existing==null) {
                itemRepository.insertBatch(batch)
            } else {
                existing.subUnit += batch.subUnit
                itemRepository.updateBatch(existing)
            }
        }
    }

    fun convertBatch(batches: List<ItemBatchEntity>, subUnitThreshold: Int, newSubUnitThreshold: Int){
        viewModelScope.launch {
            batches.forEach { batch ->
                val unit = batch.subUnit.toDouble()/subUnitThreshold
                batch.subUnit = (unit*newSubUnitThreshold).toInt()
                itemRepository.updateBatch(batch)
            }
        }
    }

    fun deleteBatch(batches: List<ItemBatchEntity>, toRemove: Int) {
        viewModelScope.launch {
            var remaining = toRemove

            batches.forEach { batch ->
                if (remaining <= 0) return@forEach

                val removeAmount = minOf(batch.subUnit, remaining)
                batch.subUnit -= removeAmount
                remaining -= removeAmount

                if (batch.subUnit == 0) {
                    itemRepository.deleteBatch(batch)
                } else {
                    itemRepository.updateBatch(batch)
                }
            }
        }
    }
}