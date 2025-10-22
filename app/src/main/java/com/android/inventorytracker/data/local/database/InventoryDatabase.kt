package com.android.inventorytracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.entities.ItemEntity

@Database(entities = [ItemEntity::class], version = 1)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun item(): ItemDao
}
