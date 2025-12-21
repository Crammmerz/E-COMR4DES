package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.ui.theme.Palette


@Composable
fun ExpiryLevels(
    modifier: Modifier = Modifier,
    itemModel: List<ItemModel>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Palette.PureWhite)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Expiry",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold, // Use SemiBold for the main header
                fontSize = 24.sp, // Approximate size for titleLarge
                color = Palette.DarkBeigeText
            )
        )
        if(itemModel.isNotEmpty()) {
            BoxWithConstraints(
                modifier = Modifier.weight(1f)
            ) {
                val spacing = 16.dp
                val rowsVisible = 1
                val totalSpacing = spacing * 0
                val cellHeight = (maxHeight - totalSpacing) / rowsVisible
                val gridHeight = cellHeight * rowsVisible + totalSpacing

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    modifier = Modifier.height(gridHeight)
                ) {
                    items(itemModel) { model ->
                        Column(
                            modifier = Modifier
                                .height(cellHeight)
                                .background(Palette.InnerTileBackground),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val imagePainter = if (model.item.imageUri != null) {
                                rememberAsyncImagePainter(
                                    model = model.item.imageUri,
                                    placeholder = painterResource(R.drawable.outline_add_photo_alternate_24),
                                    error = painterResource(R.drawable.outline_add_photo_alternate_24)
                                )
                            } else {
                                painterResource(id = R.drawable.outline_add_photo_alternate_24)
                            }

                            Image(
                                painter = imagePainter,
                                contentDescription = "Item image: ${model.item.name}",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = model.item.name,
                                modifier = Modifier.fillMaxWidth(),
                                // Apply Google Sans Medium for item name
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium, // Use Medium for item name
                                    fontSize = 16.sp, // Approximate size for bodyMedium
                                    color = Palette.DarkBeigeText
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Expires in",
                                    // Apply Google Sans Regular for detail text
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal, // Use Regular for label text
                                        fontSize = 12.sp, // Approximate size for bodySmall
                                        color = Palette.DarkBeigeText
                                    )
                                )
                                Text(
                                    text = model.expiryMessage,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(model.expiryColor.copy(alpha = 0.12f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
                                        color = model.expiryColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()){
                Text("Nothing to worry about — no alerts")
            }
        }
    }
}