package com.android.inventorytracker.data.model

import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class SortBy {
    NAME_ASC, NAME_DESC, EXPIRY_SOONEST, STOCK_LOW_HIGH, STOCK_HIGH_LOW
}
data class ItemModel(
    val item: ItemEntity,
    val batch: List<ItemBatchEntity>,
    val expiryParser: (String) -> LocalDate? = { s -> runCatching { LocalDate.parse(s) }.getOrNull() },
    val unitFormatter: (Double) -> String = { d -> java.text.DecimalFormat("#.####").format(d) }
) {
    val totalUnit: Double
        get() {
            val threshold = item.subUnitThreshold.takeIf { it != 0 } ?: 1
            return batch.sumOf { it.subUnit / threshold.toDouble() }
        }
    val totalUnitFormatted: String get() = unitFormatter(totalUnit)

    val totalSubUnit: Int get() = batch.sumOf { it.subUnit }

    val nearestExpiry: LocalDate? get() = batch.mapNotNull { expiryParser(it.expiryDate) }.minOrNull()

    fun nearestExpiryFormatted(dateFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("MMM dd, yyyy")): String =
        nearestExpiry?.let { dateFormatter?.format(it) } ?: "N/A"
}
