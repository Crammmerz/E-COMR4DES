package com.android.inventorytracker.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ItemModel(
    val item: ItemEntity,
    val batch: List<ItemBatchEntity>
) {
    val totalUnit: Double
        get() = batch.sumOf {
            it.unit.toDouble()
        }

    val nearestExpiryFormatted: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = nearestExpiryDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "N/A"

    val nearestExpiryDate: LocalDate?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = batch
            .mapNotNull { runCatching { LocalDate.parse(it.expiryDate) }.getOrNull() }
            .minOrNull()
}
