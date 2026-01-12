package com.android.inventorytracker.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.dao.UserDao
import com.android.inventorytracker.data.local.entities.UserEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InventoryDatabase {
        val dbCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Initialize the auto-increment sequence to start at 1000
                db.execSQL("INSERT OR IGNORE INTO sqlite_sequence (name, seq) VALUES ('items', 999)")
                db.execSQL("UPDATE sqlite_sequence SET seq = 999 WHERE name = 'items'")
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                // Re-apply the sequence logic if the DB is wiped/recreated
                db.execSQL("INSERT OR IGNORE INTO sqlite_sequence (name, seq) VALUES ('items', 999)")
                db.execSQL("UPDATE sqlite_sequence SET seq = 999 WHERE name = 'items'")
            }
        }

        return Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            "inventory.db"
        )
            .fallbackToDestructiveMigration() // Useful during development, dangerous in production!
            .addCallback(dbCallback)
            .build()
    }

    @Provides fun provideItemDao(db: InventoryDatabase): ItemDao = db.itemDao()
    @Provides fun provideItemBatchDao(db: InventoryDatabase): ItemBatchDao = db.itemBatchDao()
    @Provides fun provideUserDao(db: InventoryDatabase): UserDao = db.userDao()
}

