package com.android.inventorytracking.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expiry_batches",
    foreignKeys = [ForeignKey(
        entity = Item::class,
        parentColumns = ["id"],
        childColumns = ["itemId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("itemId")]
)
data class ExpiryBatch(
    @PrimaryKey(autoGenerate = true) val batchId: Int = 0,
    val itemId: Int,
    val quantity: Double,
    val expiryDate: String
)
