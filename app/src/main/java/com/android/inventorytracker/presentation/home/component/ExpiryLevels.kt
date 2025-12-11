package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.android.inventorytracker.ui.theme.LightSand

// Soft card border to match login popup
private val LumiCardBorder = Color(0xFFE7E4DD)
private val LumiCardBg = Color(0xFFFFFFFF)
private val ItemBg = Color(0xFFF9F3E6)   // very light beige for tiles

@Composable
fun ExpiryLevels(
    modifier: Modifier = Modifier,
    itemModel: List<ItemModel>
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = LumiCardBg,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, LumiCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Expiry",
                style = MaterialTheme.typography.titleMedium
            )

            BoxWithConstraints(
                modifier = Modifier.weight(1f)
            ) {
                val spacing = 12.dp
                val rowsVisible = 2
                val totalSpacing = spacing * (rowsVisible - 1)
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
                                .clip(RoundedCornerShape(16.dp))
                                .background(ItemBg)
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (model.item.imageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = model.item.imageUri,
                                        placeholder = painterResource(R.drawable.outline_add_photo_alternate_24),
                                        error = painterResource(R.drawable.outline_add_photo_alternate_24)
                                    ),
                                    contentDescription = "Selected image",
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .weight(1f),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.outline_add_photo_alternate_24),
                                    contentDescription = "Placeholder image",
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .weight(1f),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = model.item.name,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Expires in",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = model.expiryMessage,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(model.expiryColor.copy(alpha = 0.12f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
