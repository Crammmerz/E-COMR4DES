package com.android.inventorytracker.data.model

import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.ui.theme.DarkRed
import com.android.inventorytracker.ui.theme.Orange
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class SortBy {
    NAME_ASC, NAME_DESC, EXPIRY_ASCENDING, STOCK_LOW, STOCK_LOW_HIGH, STOCK_HIGH_LOW,
}
data class ItemModel(
    val item: ItemEntity,
    val batch: List<ItemBatchEntity>
) {
    companion object {
        private val df = DecimalFormat("#.####")
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }

    fun totalUnit(): Double = batch.sumOf {
        it.subUnit / (item.subUnitThreshold.takeIf { it != 0 } ?: 1).toDouble()
    }
    fun totalUnitFormatted(): String = df.format(totalUnit())

    fun totalSubUnit(): Int = batch.sumOf { it.subUnit }

    private val nearestExpiryMillis: Long? = batch.minOfOrNull { it.expiryDate }
    val nearestExpiryDate: LocalDate? = nearestExpiryMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val daysLeft: Long? = nearestExpiryDate?.let { ChronoUnit.DAYS.between(LocalDate.now(), it) }

    val nearestExpiryFormatted: String = nearestExpiryDate?.let { dateFormatter.format(it) } ?: "N/A"

    val isExpiringSoon: Boolean = nearestExpiryDate?.isBefore(
        LocalDate.now().plusDays(item.expiryThreshold.toLong())
    ) == true
    val hasExpired: Boolean = nearestExpiryDate?.isBefore(LocalDate.now()) == true
    val isLowStock: Boolean = totalUnit() <= item.unitThreshold * 0.20
    val hasNoStock: Boolean = totalUnit() <= 0.0

    val expiryMessage: String = when {
        daysLeft == null -> "N/A"
        daysLeft < 0 -> "Expired"
        daysLeft == 0L -> "Expires today"
        else -> "$daysLeft days"
    }

    val expiryColor: Color = when {
        daysLeft == null -> Color.Gray          // unknown
        daysLeft < 0 -> DarkRed                 // already expired
        daysLeft == 0L -> Color.Red             // expires today
        isExpiringSoon -> Orange              // near expiry (within a week)
        else -> Color.Gray                     // safe / normal stock
    }

    val stockColor: Color = when {
        totalUnit() <= 0.0 -> DarkRed           // no stock
        totalUnit() < item.unitThreshold * 0.20 -> Color.Red   // critically low
        totalUnit() < item.unitThreshold * 0.50 -> Orange      // warning zone
        else -> Color.Gray                     // healthy stock
    }
}

