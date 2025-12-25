package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
            .width(320.dp) // Slightly wider for better text readability
            .background(Palette.PopupSurface) // Matching your popup background
            .padding(24.dp) // Consistent with DeleteItemPopup padding
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
            verticalArrangement = Arrangement.spacedBy(16.dp) // More "breathable" spacing
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
    // Wrapped in a subtle surface to give it a "Card" feel without the heavy shadow
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                tint = iconColor,
                contentDescription = null,
                modifier = Modifier.size(10.dp) // Smaller, cleaner indicator
            )
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
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
            modifier = Modifier.padding(start = 20.dp) // Indented to align with the text above
        )
    }
}