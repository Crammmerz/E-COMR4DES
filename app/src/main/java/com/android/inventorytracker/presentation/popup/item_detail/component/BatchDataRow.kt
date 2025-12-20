package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.util.toFormattedDateString

@Composable
fun BatchDataRow(threshold: Int, batch: ItemBatchEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CalendarMonth, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            Spacer(Modifier.width(8.dp))
            Text(batch.expiryDate.toFormattedDateString(), fontFamily = GoogleSans, fontSize = 14.sp)
        }

        // Value Box
        Box(
            modifier = Modifier
                .weight(0.25f)
                .background(Color(0xFFF1F1F1), RoundedCornerShape(4.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("${batch.subUnit / threshold}", fontSize = 14.sp)
        }

        Spacer(Modifier.width(8.dp))

        // Value Box
        Box(
            modifier = Modifier
                .weight(0.25f)
                .background(Color(0xFFF1F1F1), RoundedCornerShape(4.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("${batch.subUnit}", fontSize = 14.sp)
        }
    }
}