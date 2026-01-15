package com.android.inventorytracker.data.model

import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.ui.theme.DarkRed
import com.android.inventorytracker.ui.theme.Orange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BatchModel(
    val batch: ItemBatchEntity,
    val expiryThreshold: Int,
) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    }

    val localExpiryDate: LocalDate = Instant.ofEpochMilli(batch.expiryDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val utcExpiryMillis: Long = localExpiryDate
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

    val daysLeft: Long = ChronoUnit.DAYS.between(LocalDate.now(), localExpiryDate)

    val isExpired: Boolean = daysLeft < 0
    val expiresToday: Boolean = daysLeft == 0L
    val isExpiringSoon: Boolean = daysLeft in 1..expiryThreshold.toLong()

    val color: Color = when {
        isExpired -> DarkRed
        expiresToday -> Color.Red
        isExpiringSoon -> Orange
        else -> Color.Gray
    }

    val expiryMessage: String = when {
        isExpired -> "Expired"
        expiresToday -> "Expires today"
        else -> "$daysLeft days left"
    }

    val dateFormatted: String = dateFormatter.format(localExpiryDate)
}