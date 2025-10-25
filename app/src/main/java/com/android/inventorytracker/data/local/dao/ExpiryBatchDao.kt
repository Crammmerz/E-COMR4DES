package com.android.inventorytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.inventorytracker.data.local.entities.ExpiryBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpiryBatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expiry: ExpiryBatchEntity)

    @Delete
    fun delete(expiry: ExpiryBatchEntity)

    @Update
    suspend fun update(expiry: ExpiryBatchEntity)

    @Query("SELECT * FROM expiry_batches WHERE id = :id")
    suspend fun getExpiryBatchById(id: String): ExpiryBatchEntity

    @Query("SELECT * FROM expiry_batches ORDER BY unit ASC")
    suspend fun getExpiryBatchesOrderByUnit(): Flow<List<ExpiryBatchEntity>>
}