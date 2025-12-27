package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun BatchExpirySection(model: ItemModel, modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
            Icon(Icons.Outlined.CalendarToday, null, modifier = Modifier.size(20.dp), tint = Palette.DarkBeigeText)
            Spacer(Modifier.width(10.dp))
            Text(
                "Batch & Expiry Records",
                style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Palette.DarkBeigeText)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Palette.ButtonDarkBrown, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(vertical = 12.dp, horizontal = 20.dp)
            ) {
                Text("Expiry Date", Modifier.weight(0.5f), color = Color.White, fontSize = 12.sp, fontFamily = GoogleSans)
                Text("Units", Modifier.weight(0.25f), color = Color.White, fontSize = 12.sp, fontFamily = GoogleSans)
                Text("Sub Units", Modifier.weight(0.25f), color = Color.White, fontSize = 12.sp, fontFamily = GoogleSans)
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(model.batch, key = { it.id }) { batchItem ->
                    BatchDataRow(model.item.subUnitThreshold, batchItem)
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.2f))
                }
            }
        }
    }
}