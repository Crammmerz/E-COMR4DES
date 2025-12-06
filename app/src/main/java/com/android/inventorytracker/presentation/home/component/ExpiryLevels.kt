package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.DarkRed
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.ui.theme.Orange
import com.android.inventorytracker.util.toLocalDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Composable
fun ExpiryLevels(modifier: Modifier, itemModel: List<ItemModel>) {
    Column (modifier = modifier.background(Color.White).padding(50.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "Expiry", style = MaterialTheme.typography.titleLarge)
        BoxWithConstraints(
            modifier = Modifier.weight(1f)
        ) {

            //Calculation for Responsive Layout in Different Device Dimensions
            val spacing = 12.dp
            val rowsVisible = 2
            val totalSpacing = spacing * (rowsVisible - 1)
            val cellHeight = (maxHeight - totalSpacing) / rowsVisible
            val gridHeight = cellHeight * rowsVisible + totalSpacing

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .height(gridHeight)
            ) {
                items(itemModel) { model ->
                    val daysLeft = model.nearestExpiry?.toLocalDate()?.let { date ->
                        ChronoUnit.DAYS.between(LocalDate.now(), date)
                    } ?: 0

                    val (expiryMessage, messageColor) = when {
                        daysLeft < 0 -> "Expired" to DarkRed
                        daysLeft == 0L -> "Expires today" to Color.Red
                        else -> "$daysLeft days" to Orange
                    }

                    Column(
                        modifier = Modifier
                            .height(cellHeight)
                            .background(LightSand)
                            .padding(10.dp)
                    ) {
                        Text(model.item.name)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Expires In", modifier = Modifier.weight(1f))
                            Text(
                                expiryMessage,
                                modifier = Modifier.background(messageColor).padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}