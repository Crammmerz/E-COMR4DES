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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun BatchExpirySection(model: ItemModel, modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
            Icon(Icons.Outlined.CalendarToday, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Batch Expiry Information", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            // Dark Brown Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Palette.ButtonDarkBrown, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text("Expiry Date", Modifier.weight(0.5f), color = Color.White, fontSize = 12.sp)
                Text("Unit", Modifier.weight(0.25f), color = Color.White, fontSize = 12.sp)
                Text("Sub Unit", Modifier.weight(0.25f), color = Color.White, fontSize = 12.sp)
            }

            LazyColumn {
                items(model.batch, key = { it.id }) {
                    BatchDataRow(model.item.subUnitThreshold, it)
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.3f))
                }
            }
        }
    }
}