package com.android.inventorytracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.dao.UserDao
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.local.entities.UserEntity

@Database(
    entities = [ItemEntity::class, ItemBatchEntity::class, UserEntity::class],
    version = 8,
    exportSchema = false
)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemBatchDao(): ItemBatchDao
    abstract fun userDao(): UserDao
}
