package com.android.inventorytracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.inventorytracker.data.local.dao.ItemDao
import com.android.inventorytracker.data.local.dao.ItemBatchDao
import com.android.inventorytracker.data.local.dao.UserDao
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import kotlin.concurrent.Volatile
@Database(
    entities = [ItemEntity::class, ItemBatchEntity::class, UserEntity::class],
    version = 6
)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemBatchDao(): ItemBatchDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: InventoryDatabase? = null
        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventory.db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.userDao()?.insert(
                                    UserEntity(
                                        username = "admin",
                                        passwordHash = hashPassword("admin"),
                                        role = "ADMIN"
                                    )
                                )
                                INSTANCE?.userDao()?.insert(
                                    UserEntity(
                                        username = "staff",
                                        passwordHash = hashPassword("staff"),
                                        role = "STAFF"
                                    )
                                )
                            }
                        }
                    })
                    .build().also { INSTANCE = it }
            }
        }
        private fun hashPassword(password: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}
