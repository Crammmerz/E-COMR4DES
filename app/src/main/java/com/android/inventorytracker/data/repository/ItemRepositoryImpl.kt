package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.ItemWithBatches
import com.android.inventorytracker.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao,
    private val batchDao: ItemBatchDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ItemRepository {

    override fun observeItems(): Flow<List<ItemEntity>> =
        itemDao.getItemOrderByNameAsc().flowOn(ioDispatcher)

    override fun observeBatches(): Flow<List<ItemBatchEntity>> =
        batchDao.getBatchesOrderByUnitAsc().flowOn(ioDispatcher)

    override fun observeItemModels(): Flow<List<ItemModel>> =
        itemDao.observeItemsWithBatches()
            .map { list -> list.map { it.toItemModel() } }
            .flowOn(ioDispatcher)

    private fun ItemWithBatches.toItemModel(): ItemModel =
        ItemModel(
            item = this.item,
            batch = this.batches,
        )

    override suspend fun insertItem(item: ItemEntity) = withContext(ioDispatcher) {
        itemDao.insertItem(item)
    }

    override suspend fun updateItem(item: ItemEntity) = withContext(ioDispatcher) {
        itemDao.updateItem(item)
    }

    override suspend fun deleteItem(item: ItemEntity) = withContext(ioDispatcher) {
        itemDao.deleteItem(item)
    }

    override suspend fun findBatch(itemId: Int, expiryDate: Long): ItemBatchEntity? =
        withContext(ioDispatcher) {
            batchDao.getBatch(itemId, expiryDate)
        }

    override suspend fun insertBatch(batch: ItemBatchEntity) = withContext(ioDispatcher) {
        batchDao.insertBatch(batch)
    }

    override suspend fun updateBatch(batch: ItemBatchEntity) = withContext(ioDispatcher) {
        batchDao.updateBatch(batch)
    }

    override suspend fun deleteBatch(batch: ItemBatchEntity) = withContext(ioDispatcher) {
        batchDao.deleteBatch(batch)
    }
}