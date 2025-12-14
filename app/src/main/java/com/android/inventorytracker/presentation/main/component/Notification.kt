package com.android.inventorytracker.presentation.main.component

import android.app.Notification
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    expiryItems: List<ItemModel>,
    stockItems: List<ItemModel>,
) {
    val expiredItems = expiryItems.filter { it.hasExpired }
    val expiringItems = expiryItems.filter { it.isExpiringSoon && !it.hasExpired }
    val noStockItems = stockItems.filter { it.hasNoStock }
    val lowStockItems = stockItems.filter { it.isLowStock && !it.hasNoStock }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Palette.PureWhite)
            .width(300.dp)
            .padding(10.dp)
    ) {
        Text(text = "Notification")
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(expiredItems) { model ->
                NotificationItem(
                    iconColor = model.expiryColor,
                    title = "Expired Alerts",
                    message = "${model.item.name} has expired on ${model.nearestExpiryFormatted}"
                )
            }
            items(noStockItems) { model ->
                NotificationItem(
                    iconColor = model.stockColor,
                    title = "No Stock Alerts",
                    message = "${model.item.name} has no stock (${model.totalUnitFormatted()} units left)"
                )
            }
            items(expiringItems) { model ->
                NotificationItem(
                    iconColor = model.expiryColor,
                    title = "Near Expiry Alerts",
                    message = "${model.item.name} expires on ${model.nearestExpiryFormatted}"
                )
            }
            items(lowStockItems) { model ->
                NotificationItem(
                    iconColor = model.stockColor,
                    title = "Low Stock Alerts",
                    message = "${model.item.name} is running low (${model.totalUnitFormatted()} units left)"
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    iconColor: Color,
    title: String,
    message: String
) {
    Column {
        Row {
            Icon(
                imageVector = Icons.Default.Circle,
                tint = iconColor,
                contentDescription = null
            )
            Text(text = title)
        }
        Text(text = message)
    }
}
