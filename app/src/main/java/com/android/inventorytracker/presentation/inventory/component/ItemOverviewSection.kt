package com.android.inventorytracker.presentation.inventory.component

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.R
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.ui.theme.LightSand

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemOverviewSection(
    item: ItemEntity, batch: ItemBatchEntity?, modifier: Modifier = Modifier){
    val raw = batch?.expiryDate ?: ""
    val date = when {
        raw.isBlank() -> "N/A"
        else -> raw
    }

    val unitCount = if (item.unitThreshold > 0 && batch!=null) {
        batch.subUnit.toFloat() / item.unitThreshold
    } else 0f

    Row(
        modifier = modifier
            .height(75.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp))
            .padding(horizontal = 15.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.haahahaahha),
            contentDescription = "Placeholder image",
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .size(64.dp)
        )
        Spacer(Modifier.weight(0.1f))
        Text(
            text = item.name,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(LightSand)
                .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(horizontal = 7.dp, vertical = 7.dp)
        )
        Text(
            text = date,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(0.75f)
                .clip(RoundedCornerShape(5.dp))
                .background(LightSand)
                .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(horizontal = 7.dp, vertical = 7.dp)
        )
        Text(
            text = String.format("%.2f", unitCount),
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(0.5f)
                .clip(RoundedCornerShape(5.dp))
                .background(LightSand)
                .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(horizontal = 7.dp, vertical = 7.dp)
        )
        Text(
            text = "+",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(LightSand)
                .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
                .weight(0.25f)
        )
        Text(
            text = "-",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(LightSand)
                .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
                .weight(0.25f)
        )
        Text(
            text = "View More",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(LightSand)
                .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                .padding(horizontal = 7.dp, vertical = 7.dp)
                .weight(0.5f)

        )
    }
}