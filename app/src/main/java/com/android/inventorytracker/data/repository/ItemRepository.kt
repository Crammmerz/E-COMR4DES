package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun observeItems(): Flow<List<ItemEntity>>

    fun observeBatches(): Flow<List<ItemBatchEntity>>

    fun observeItemModels(): Flow<List<ItemModel>>

    suspend fun insertItem(item: ItemEntity)
    suspend fun updateItem(item: ItemEntity)
    suspend fun deleteItem(item: ItemEntity)

    suspend fun findBatch(itemId: Int, expiryDate: Long): ItemBatchEntity?
    suspend fun insertBatch(batch: ItemBatchEntity)
    suspend fun updateBatch(batch: ItemBatchEntity)
    suspend fun deleteBatch(batch: ItemBatchEntity)
}