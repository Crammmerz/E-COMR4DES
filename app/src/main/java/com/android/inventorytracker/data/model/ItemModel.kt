package com.android.inventorytracker.data.model

import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class SortBy {
    NAME_ASC, NAME_DESC, EXPIRY_SOONEST, STOCK_LOW_HIGH, STOCK_HIGH_LOW
}
data class ItemModel(
    val item: ItemEntity,
    val batch: List<ItemBatchEntity>
) {
    companion object {
        private val df = DecimalFormat("#.####")
        @RequiresApi(Build.VERSION_CODES.O)
        private val expiryFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }

    val totalUnit: Double
        get() = batch.sumOf { it.subUnit / item.subUnitThreshold.toDouble() }

    val totalSubUnit: Int
        get() = batch.sumOf { it.subUnit }

    val totalUnitFormatted: String
        get() = df.format(totalUnit)

    val nearestExpiry: LocalDate?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = batch.mapNotNull { runCatching { LocalDate.parse(it.expiryDate) }.getOrNull() }
            .minOrNull()

    val nearestExpiryFormatted: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = nearestExpiry?.format(expiryFormatter) ?: "N/A"
}
