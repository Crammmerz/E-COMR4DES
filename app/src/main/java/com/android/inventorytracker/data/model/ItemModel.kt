package com.android.inventorytracker.data.model

import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.ui.theme.DarkRed
import com.android.inventorytracker.ui.theme.Orange
import java.text.DecimalFormat

enum class SortBy {
    NAME_ASC, NAME_DESC, EXPIRY_ASCENDING, STOCK_LOW, STOCK_LOW_HIGH, STOCK_HIGH_LOW,
}

data class ItemModel(
    val item: ItemEntity,
    val batch: List<ItemBatchEntity>
) {
    companion object {
        private val df = DecimalFormat("#.####")
    }
    val batchModels: List<BatchModel> = batch.map {
        BatchModel(it, item.expiryThreshold)
    }
    fun expiringBatches(): List<BatchModel> =
        batchModels.filter { it.isExpired || it.isExpiringSoon || it.expiresToday }

    fun expiringSoonBatches(): List<BatchModel> =
        batchModels.filter { it.isExpiringSoon }

    fun expiringTodayBatches(): List<BatchModel> =
        batchModels.filter { it.expiresToday }
    fun expiredBatches(): List<BatchModel> =
        batchModels.filter { it.isExpired }
    fun nearestExpiry(): BatchModel? = batchModels.minByOrNull { it.localExpiryDate }

    val hasExpired: Boolean = expiredBatches().isNotEmpty()
    val isExpiringToday: Boolean = expiringTodayBatches().isNotEmpty() && !hasExpired
    val isExpiringSoon: Boolean = expiringBatches().isNotEmpty() && !hasExpired


    fun totalSubUnit(): Int = batch.sumOf { it.subUnit }

    fun totalUnit(): Double = batch.sumOf {
        it.subUnit / (item.subUnitThreshold.takeIf { it != 0 } ?: 1).toDouble()
    }

    fun totalUnitFormatted(): String = df.format(totalUnit())

    // --- Visuals ---
    val isNearLowStock: Boolean = totalUnit() <= item.unitThreshold * 0.50
    val isLowStock: Boolean = totalUnit() <= item.unitThreshold
    val hasNoStock: Boolean = totalUnit() <= 0.0

    val stockColor: Color = when {
        hasNoStock -> DarkRed
        isLowStock -> Orange
        isNearLowStock -> Color.Red
        else -> Color.Gray
    }
}