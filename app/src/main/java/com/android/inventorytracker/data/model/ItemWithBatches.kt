package com.android.inventorytracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity

data class ItemWithBatches(
    @Embedded val item: ItemEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val batches: List<ItemBatchEntity>
)

