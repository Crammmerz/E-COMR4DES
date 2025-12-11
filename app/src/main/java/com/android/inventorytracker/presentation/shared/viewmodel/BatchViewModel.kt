package com.android.inventorytracker.presentation.shared.viewmodel

import android.util.Log
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
import kotlin.math.roundToInt

@HiltViewModel
class BatchViewModel @Inject constructor(
    private val itemRepository: ItemRepository
): ViewModel() {

    fun onStoreBatch(batch: ItemBatchEntity) {
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

    fun onConvertBatch(batches: List<ItemBatchEntity>, subUnitThreshold: Int, newSubUnitThreshold: Int){
        viewModelScope.launch {
            batches.forEach { batch ->
                val unit = batch.subUnit.toDouble()/subUnitThreshold
                batch.subUnit = maxOf(1, (unit * newSubUnitThreshold).roundToInt())
                itemRepository.updateBatch(batch)
            }
        }
    }

    fun onDeductStock(
        batches: List<ItemBatchEntity>,
        toRemove: Int
    ) {
        viewModelScope.launch {
            var remaining = toRemove
            Log.d("DeductStock", "Remaining: $remaining")
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

    fun onTargetedDeductStock(
        batch: ItemBatchEntity,
        toRemove: Int
    ) {
        viewModelScope.launch {
            batch.subUnit -= toRemove

            if (batch.subUnit == 0) {
                itemRepository.deleteBatch(batch)
            } else {
                itemRepository.updateBatch(batch)
            }
        }
    }
}