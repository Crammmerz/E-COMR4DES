package com.android.inventorytracker.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
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
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InventoryDatabase =
        Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            "inventory.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        // Use the singleton DB instance instead of rebuilding
                        val database = provideDatabase(context)
                        database.userDao().insert(
                            UserEntity(1,"admin", hashPassword("admin"), "ADMIN")
                        )
                        database.userDao().insert(
                            UserEntity(2,"staff", hashPassword("staff"), "STAFF")
                        )
                    }
                }
            })
            .build()

    @Provides fun provideItemDao(db: InventoryDatabase): ItemDao = db.itemDao()
    @Provides fun provideItemBatchDao(db: InventoryDatabase): ItemBatchDao = db.itemBatchDao()
    @Provides fun provideUserDao(db: InventoryDatabase): UserDao = db.userDao()

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

