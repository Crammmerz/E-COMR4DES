package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    expiryItems: List<ItemModel>,
    stockItems: List<ItemModel>,
) {
    // 🔹 Original filter logic preserved
    val expiredItems = expiryItems.filter { it.hasExpired }
    val expiringItems = expiryItems.filter { it.isExpiringSoon && !it.hasExpired }
    val noStockItems = stockItems.filter { it.hasNoStock }
    val lowStockItems = stockItems.filter { it.isLowStock && !it.hasNoStock }

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
            items(expiredItems) { model ->
                NotificationItem(
                    iconColor = model.expiryColor,
                    title = "Expired Alert: ${model.item.name}",
                    message = "${model.item.name} has expired on ${model.nearestExpiryFormatted}"
                )
            }
            items(noStockItems) { model ->
                NotificationItem(
                    iconColor = model.stockColor,
                    title = "No Stock Alert: ${model.item.name}",
                    message = "${model.item.name} has no stock (${model.totalUnitFormatted()} units left)"
                )
            }
            items(expiringItems) { model ->
                NotificationItem(
                    iconColor = model.expiryColor,
                    title = "Near Expiry Alert: ${model.item.name}",
                    message = "${model.item.name} expires on ${model.nearestExpiryFormatted}"
                )
            }
            items(lowStockItems) { model ->
                NotificationItem(
                    iconColor = model.stockColor,
                    title = "Low Stock Alert: ${model.item.name}",
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
                modifier = Modifier.align(Alignment.CenterStart).padding(end = 50.dp) // Space para sa date
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    tint = iconColor,
                    contentDescription = null,
                    modifier = Modifier.size(8.dp)
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

        Text(
            text = message,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            ),
            modifier = Modifier.padding(start = 18.dp)
        )
    }
}