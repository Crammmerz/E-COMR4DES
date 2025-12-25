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
import com.android.inventorytracker.ui.theme.GoogleSans


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
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
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
                                style = TextStyle(
                                    fontFamily = GoogleSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
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
                                    style = TextStyle(
                                        fontFamily = GoogleSans,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
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
                                        fontFamily = GoogleSans,
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
                Text(
                    text = "Nothing to worry about — no alerts",
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