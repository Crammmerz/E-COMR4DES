package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun StockLevels(modifier: Modifier, itemModel: List<ItemModel>) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Palette.PureWhite)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Text
        Text(
            text = "Stocks",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = Palette.DarkBeigeText
            )
        )

        if (itemModel.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(itemModel) { model ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Item Name
                        Text(
                            text = model.item.name,
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Palette.DarkBeigeText
                            )
                        )

                        // Progress Bar Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            // Linear Progress Indicator
                            LinearProgressIndicator(
                                progress = {
                                    (model.totalUnit().toFloat() / model.item.unitThreshold.toFloat())
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .height(8.dp),
                                color = model.stockColor,
                                trackColor = Palette.BeigeProgressTrack
                            )

                            // Count Text
                            Text(
                                text = "${model.totalUnitFormatted()} / ${model.item.unitThreshold}",
                                modifier = Modifier.width(100.dp),
                                textAlign = TextAlign.End,
                                style = TextStyle(
                                    fontFamily = GoogleSans,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Palette.LightBeigeText
                                )
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                Text(
                    text = "You're stocked up — no low stock alerts",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Palette.LightBeigeText
                    )
                )
            }
        }
    }
}