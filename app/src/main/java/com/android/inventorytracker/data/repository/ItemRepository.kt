package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val batchDao: ItemBatchDao
) {
    // Returns items sorted alphabetically by name
    fun getItemList(): Flow<List<ItemEntity>> = itemDao.getItemOrderByNameAsc()

    fun getBatchList(): Flow<List<ItemBatchEntity>> = batchDao.getBatchesOrderByUnitAsc()

    suspend fun findBatch(batch: ItemBatchEntity) =
        batchDao.getBatch(batch.itemId, batch.expiryDate)

    suspend fun insertItem(item: ItemEntity) = itemDao.insert(item)

    suspend fun updateItem(item: ItemEntity) = itemDao.updateItem(item)

    suspend fun deleteItem(item: ItemEntity) = itemDao.deleteItem(item)

    suspend fun insertBatch(batch: ItemBatchEntity) = batchDao.insertBatch(batch)

    suspend fun updateBatch(batch: ItemBatchEntity) = batchDao.updateBatch(batch)

    suspend fun deleteBatch(batch: ItemBatchEntity) = batchDao.deleteBatch(batch)
}