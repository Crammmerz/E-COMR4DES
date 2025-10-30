package com.android.inventorytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemBatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expiry: ItemBatchEntity)

    @Delete
    fun delete(expiry: ItemBatchEntity)

    @Update
    suspend fun update(expiry: ItemBatchEntity)

    @Query("SELECT * FROM itemBatch WHERE id = :id")
    suspend fun getExpiryBatchById(id: String): ItemBatchEntity

    @Query("SELECT * FROM itemBatch ORDER BY subUnit ASC")
    fun getBatchesOrderByUnit(): Flow<List<ItemBatchEntity>>
}