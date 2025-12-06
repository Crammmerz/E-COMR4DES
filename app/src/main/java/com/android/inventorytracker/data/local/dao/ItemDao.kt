package com.android.inventorytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemWithBatches
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Int): ItemEntity?

    @Query("SELECT items.* FROM items ORDER BY items.name ASC")
    fun getItemOrderByNameAsc(): Flow<List<ItemEntity>>

    @Transaction
    @Query("""
        SELECT * FROM items
        ORDER BY name COLLATE NOCASE
    """)
    fun observeItemsWithBatches(): Flow<List<ItemWithBatches>>
}
