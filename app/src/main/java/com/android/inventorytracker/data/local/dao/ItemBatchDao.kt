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
    suspend fun insertBatch(batch: ItemBatchEntity)

    @Delete
    fun deleteBatch(batch: ItemBatchEntity)

    @Update
    suspend fun updateBatch(batch: ItemBatchEntity)

    @Query("SELECT * FROM itemBatch WHERE itemId = :itemId AND expiryDate = :expiryDate")
    suspend fun getBatch(itemId: Int, expiryDate: String): ItemBatchEntity?

    @Query("SELECT * FROM itemBatch ORDER BY expiryDate ASC")
    fun getBatchesOrderByUnit(): Flow<List<ItemBatchEntity>>
}