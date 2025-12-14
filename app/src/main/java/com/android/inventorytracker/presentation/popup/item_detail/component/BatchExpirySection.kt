package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun BatchExpirySection(model: ItemModel, modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Batch Expiry Information",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
        ) {
            Row(Modifier.padding(10.dp)) {
                Text("Expiry Date", Modifier.weight(0.5f))
                Text("Unit", Modifier.weight(0.25f), textAlign = TextAlign.Center)
                Text("Sub Unit", Modifier.weight(0.25f), textAlign = TextAlign.Center)
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))

            LazyColumn {
                items(model.batch, key = { it.id }) {
                    BatchDataRow(model.item.subUnitThreshold, it)
                }
            }
        }
    }
}
