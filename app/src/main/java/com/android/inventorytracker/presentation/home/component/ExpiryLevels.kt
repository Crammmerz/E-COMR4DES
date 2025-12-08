package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.DarkRed
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.ui.theme.Orange
import com.android.inventorytracker.util.toLocalDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Composable
fun ExpiryLevels(modifier: Modifier, itemModel: List<ItemModel>) {
    Column (modifier = modifier
        .background(Color.White)
        .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                    Column(
                        Modifier
                            .height(cellHeight)
                            .background(LightSand)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(model.item.imageUri != null){
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = model.item.imageUri,
                                    placeholder = painterResource(R.drawable.outline_add_photo_alternate_24),
                                    error = painterResource(R.drawable.outline_add_photo_alternate_24)
                                ),
                                contentDescription = "Selected image",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .weight(1f)
                                    .clip(RoundedCornerShape(5.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.outline_add_photo_alternate_24),
                                contentDescription = "Placeholder image",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .weight(1f)
                                    .clip(RoundedCornerShape(5.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(text = model.item.name, modifier = Modifier.fillMaxWidth())
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Expires In", modifier = Modifier.weight(1f))
                            Text(
                                text = model.expiryMessage,
                                modifier = Modifier
                                    .background(model.expiryColor)
                                    .padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}