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
                db.execSQL("INSERT OR IGNORE INTO sqlite_sequence (name, seq) VALUES ('items', 999)")
                db.execSQL("UPDATE sqlite_sequence SET seq = 999 WHERE name = 'items'")
            }
        }

        val db = Room.databaseBuilder(context, InventoryDatabase::class.java, "inventory.db")
            .fallbackToDestructiveMigration()
            .addCallback(dbCallback)
            .build()

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                if (db.userDao().getCount() == 0) {
                    db.withTransaction {
                        db.userDao().insert(UserEntity(1, "admin", hashPassword("admin"), "ADMIN"))
                        db.userDao().insert(UserEntity(2, "staff", hashPassword("staff"), "STAFF"))
                    }
                }
            } catch (e: Exception) {
                Log.e("DatabaseModule", "Seeding failed", e)
            }
        }

        return db
    }

    @Provides fun provideItemDao(db: InventoryDatabase): ItemDao = db.itemDao()
    @Provides fun provideItemBatchDao(db: InventoryDatabase): ItemBatchDao = db.itemBatchDao()
    @Provides fun provideUserDao(db: InventoryDatabase): UserDao = db.userDao()

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

