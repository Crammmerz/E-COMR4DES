package com.android.inventorytracker.presentation.shared.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.BatchOperation
import com.android.inventorytracker.data.model.UndoModel
import com.android.inventorytracker.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
            addGatheringUndo(batch.copy(), BatchOperation.ADD)
            if (existing==null) {
                itemRepository.insertBatch(batch)
            } else {
                val newSubUnit = existing.subUnit.toLong() + batch.subUnit.toLong()
                if (newSubUnit > Int.MAX_VALUE) {
                    throw IllegalStateException("Integer limit exceeded for subUnit")
                }
                existing.subUnit = newSubUnit.toInt()
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

    fun onDeductStock(batches: List<ItemBatchEntity>, toRemove: Int) {
        viewModelScope.launch {
            var remaining = toRemove
            batches.forEach { batch ->
                if (remaining <= 0) return@forEach

                val removeAmount = minOf(batch.subUnit, remaining)

                // Create a "History" version that only contains the amount we took
                val undoSnapshot = batch.copy(subUnit = removeAmount)

                batch.subUnit -= removeAmount
                remaining -= removeAmount

                if (batch.subUnit == 0) {
                    itemRepository.deleteBatch(batch)
                } else {
                    itemRepository.updateBatch(batch)
                }

                addGatheringUndo(undoSnapshot, BatchOperation.DEDUCT)
            }
        }
    }

    fun onTargetedDeductStock(
        batch: ItemBatchEntity,
        toRemove: Int
    ) {
        viewModelScope.launch {
            addGatheringUndo(batch.copy(), BatchOperation.DEDUCT)
            batch.subUnit -= toRemove

            if (batch.subUnit == 0) {
                itemRepository.deleteBatch(batch)
            } else {
                itemRepository.updateBatch(batch)
            }
        }
    }

    private var _gatheringBatch = mutableListOf<ItemBatchEntity>()
    var pendingUndo = mutableStateListOf<UndoModel>()
        private set

    private var debounceJob: Job? = null

    fun addGatheringUndo(batch: ItemBatchEntity, operation: BatchOperation) {
        _gatheringBatch.add(batch)

        Log.d("Gathering", _gatheringBatch.toString())
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500L)
            commitGatheredToPending(operation)
        }
    }

    private fun commitGatheredToPending(operation: BatchOperation) {
        if (_gatheringBatch.isNotEmpty()) {
            val batches = _gatheringBatch.toList()

            pendingUndo.add(0, UndoModel(
                batches = batches,
                operation = operation
            ))
            Log.d("Pending", pendingUndo.toString())
            _gatheringBatch.clear()
        }
    }

    fun doUndo() {
        val undo = pendingUndo.firstOrNull() ?: return

        viewModelScope.launch {
            undo.batches.forEach { batch ->
                val existing = itemRepository.findBatch(batch.itemId, batch.expiryDate)
                when (undo.operation) {
                    BatchOperation.DEDUCT -> {
                        if (existing != null) {
                            existing.subUnit += batch.subUnit
                            itemRepository.updateBatch(existing)
                        } else {
                            // If the batch was deleted because it hit 0, we re-insert it
                            itemRepository.insertBatch(batch)
                        }
                    }
                    BatchOperation.ADD -> {
                        if (existing != null) {
                            val newCount = existing.subUnit - batch.subUnit
                            if (newCount <= 0) {
                                // If we subtract what was added and nothing is left, delete it
                                itemRepository.deleteBatch(existing)
                            } else {
                                existing.subUnit = newCount
                                itemRepository.updateBatch(existing)
                            }
                        }
                    }
                }
            }
            pendingUndo.removeAt(0)
        }
    }
}