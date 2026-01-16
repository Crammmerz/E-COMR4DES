package com.android.inventorytracker.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): InventoryDatabase {
        // Read the dynamic database name from preferences
        val prefs = context.getSharedPreferences("backup_prefs", Context.MODE_PRIVATE)
        val currentDbName = prefs.getString("current_db_name", "inventory.db") ?: "inventory.db"

        Log.d("DatabaseModule", "Opening database file: $currentDbName")

        return Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            currentDbName
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides fun provideItemDao(db: InventoryDatabase): ItemDao = db.itemDao()
    @Provides fun provideItemBatchDao(db: InventoryDatabase): ItemBatchDao = db.itemBatchDao()
    @Provides fun provideUserDao(db: InventoryDatabase): UserDao = db.userDao()
}
