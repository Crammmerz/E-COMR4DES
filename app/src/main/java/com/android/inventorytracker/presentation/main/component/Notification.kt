package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.RunningWithErrors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Orange
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    expiryItems: List<ItemModel>,
    stockModel: List<ItemModel>
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(Palette.PopupSurface)
            .padding(24.dp)
    ) {
        Text(
            text = "Notifications",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Palette.ButtonDarkBrown
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(expiryItems) { model ->
                val relevantBatches = model.batchModels.filter {
                    it.isExpired || it.isExpiringSoon || it.expiresToday
                }

                if (relevantBatches.isNotEmpty()) {

                    val expiryTitle = when {
                        model.hasExpired -> "Expired: ${model.item.name}"
                        model.isExpiringToday -> "Expires Today: ${model.item.name}"
                        else -> "Expiring Soon: ${model.item.name}"
                    }

                    val (expiryIcon, expiryColor) = when {
                        model.hasExpired -> Icons.Default.EventBusy to Palette.AlertRed
                        model.isExpiringToday -> Icons.Default.RunningWithErrors to Color.Red
                        else -> Icons.Default.HistoryToggleOff to Orange
                    }

                    val expiryMessages = model.batchModels
                        .filter { it.isExpired || it.isExpiringSoon || it.expiresToday }
                        .map { batch ->
                            when {
                                batch.isExpired -> "Batch expired on ${batch.dateFormatted}"
                                batch.expiresToday -> "Batch expires today"
                                else -> "${batch.expiryMessage} before Batch expires (${batch.dateFormatted})"
                            }
                        }


                    NotificationItem(
                        title = expiryTitle,
                        expiryIcon = expiryIcon,
                        iconColor = expiryColor,
                        expiryMessage = expiryMessages,
                        stockIcon = expiryIcon,
                        stockMessage = ""
                    )
                }
            }

            items(stockModel) { model ->
                if (model.hasNoStock || model.isLowStock || model.isNearLowStock) {
                    val (stockIcon, stockColor) = when {
                        model.hasNoStock -> Icons.Default.Inventory2 to Palette.AlertRed
                        model.isLowStock -> Icons.Default.ProductionQuantityLimits to Orange
                        else -> Icons.Default.Notifications to Color.Yellow
                    }

                    val (title, stockMessage) = when {
                        model.hasNoStock -> "Out of Stock" to "${model.item.name} has 0 units left."
                        model.isLowStock -> "Low Stock" to "${model.item.name} is running low (${model.totalUnitFormatted()} left)."
                        else -> "Stock Warning" to "${model.item.name} is approaching low stock limit."
                    }

                    NotificationItem(
                        title = title,
                        expiryIcon = stockIcon,
                        iconColor = stockColor,
                        expiryMessage = emptyList(),
                        stockIcon = stockIcon,
                        stockMessage = stockMessage
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    title: String,
    expiryMessage: List<String>,
    stockMessage: String = "",
    expiryIcon: ImageVector,
    stockIcon: ImageVector,
    iconColor: Color,
) {
    // 🔹 Logic para sa Dynamic Timestamp (MM/dd o HH:mm)
    val timestamp = remember {
        val now = Calendar.getInstance()
        val notifTime = Calendar.getInstance() // Dito dapat ang actual generation time kung may data field ka

        val isSameDay = now.get(Calendar.YEAR) == notifTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == notifTime.get(Calendar.DAY_OF_YEAR)

        if (isSameDay) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(notifTime.time)
        } else {
            SimpleDateFormat("MM/dd", Locale.getDefault()).format(notifTime.time)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // 🔹 Container para sa Title at Date sa dulo
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 50.dp) // Space para sa date
            ) {
                Icon(
                    imageVector = if (expiryMessage.isNotEmpty()) expiryIcon else stockIcon,
                    tint = iconColor,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                    maxLines = 1
                )
            }

            // 🔹 Timestamp sa Upper Right
            Text(
                text = timestamp,
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontSize = 11.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if(expiryMessage.isNotEmpty())
            Text(
                    text =
                        expiryMessage.first(),
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        lineHeight = 18.sp
                    ),
                    maxLines = 1 // Keeps the notification compact
                )
//            expiryMessage.forEach { it ->
//                Text(
//                    text = it,
//                    style = TextStyle(
//                        fontFamily = GoogleSans,
//                        fontSize = 13.sp,
//                        color = Color.Gray,
//                        lineHeight = 18.sp
//                    ),
//                    maxLines = 1 // Keeps the notification compact
//                )
//            }

        if (stockMessage.isNotEmpty()) {
            Text(
                text = stockMessage,
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                ),
                maxLines = 1 // Keeps the notification compact
            )
        }
    }
}