package com.android.inventorytracker.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "expiry_batches",
    foreignKeys = [ForeignKey(
        entity = ItemEntity::class,
        parentColumns = ["id"],
        childColumns = ["itemId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("itemId")]
)
data class ExpiryBatch(
    @PrimaryKey(autoGenerate = true) val batchId: Int = 0,
    val itemId: Int,
    val unit: Double,
    val subUnit: Int,
    val expiryDate: LocalDate
)
