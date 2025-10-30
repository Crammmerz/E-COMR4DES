package com.android.inventorytracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import kotlin.concurrent.Volatile

@Database(entities = [ItemEntity::class, ItemBatchEntity::class], version = 3)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemBatchDao(): ItemBatchDao

    companion object {
        @Volatile private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                                context.applicationContext,
                                InventoryDatabase::class.java,
                                "inventory.db"
                            )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE=it }
            }
        }
    }
}
