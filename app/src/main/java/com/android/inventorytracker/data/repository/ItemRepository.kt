package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import kotlinx.coroutines.flow.Flow


class ItemRepository(private val itemDao: ItemDao, private val itemBatchDao: ItemBatchDao) {
    // Returns items sorted alphabetically by name
    fun getItemList(): Flow<List<ItemEntity>> = itemDao.getItemOrderByName()
    fun getBatchList(): Flow<List<ItemBatchEntity>> = itemBatchDao.getBatchesOrderByUnit()

    suspend fun addNewItem(item: ItemEntity) {
        itemDao.insert(item)
    }

    suspend fun updateItem(item: ItemEntity){
        itemDao.updateItem(item)
    }
}