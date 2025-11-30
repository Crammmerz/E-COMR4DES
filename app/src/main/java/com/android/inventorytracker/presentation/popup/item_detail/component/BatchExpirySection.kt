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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel

@Composable
fun BatchExpirySection(
    model: ItemModel,
    modifier: Modifier
){
    Column (modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Batch Expiry Information",
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal= 5.dp)
        )
        Column (
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
        ) {
            Row (Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                Text(
                    text = "Expiry Date",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = "Unit",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.25f)
                )
                Text(
                    text = "Sub Unit",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.25f)
                )
            }
            HorizontalDivider(color = Color.Black, thickness = 1.dp)
            if(model.batch.isNotEmpty()){
                LazyColumn(Modifier.padding(horizontal = 5.dp)) {
                    items(items = model.batch, key = {it.id}){ batch ->
                        BatchDataRow(model.item.subUnitThreshold,batch)
                    }
                }
            }
        }
    }
}