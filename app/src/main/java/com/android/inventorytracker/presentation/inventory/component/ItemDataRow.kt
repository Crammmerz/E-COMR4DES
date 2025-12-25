package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_insertion.BatchInsertionPopup
import com.android.inventorytracker.presentation.popup.batch_targeted_removal.BatchTargetedRemoval
import com.android.inventorytracker.presentation.popup.item_detail.ItemDetailPopup
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

val SurfaceWhite = Color.White
val LightBeigeField = Color(0xFFF2ECE6)
val AccentBrown = Color(0xFFC19A6B)
val ActionBrown = Color(0xFF8D6E63)
val BorderColor = Color(0xFFE0E0E0)

@Composable
fun ItemDataRow(
    modifier: Modifier = Modifier,
    model: ItemModel,
    itemViewModel: ItemViewModel,
) {
    var showItemDetail by rememberSaveable { mutableStateOf(false) }
    var showInsertBatch by rememberSaveable { mutableStateOf(false) }
    var showDeleteBatch by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // 1. Image (size 48.dp)
            val painter = if (model.item.imageUri != null) {
                rememberAsyncImagePainter(model = model.item.imageUri)
            } else {
                painterResource(id = R.drawable.outline_add_photo_alternate_24)
            }

            Image(
                painter = painter,
                contentDescription = "Item Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            )

            // 2. Item Name (weight 3.0f)
            DataFieldBox(
                text = model.item.name,
                backgroundColor = LightBeigeField,
                textColor = Color.Black,
                modifier = Modifier.weight(3.0f)
            )

            // 3. Expiry (weight 1.2f)
            DataFieldBox(
                text = if (model.nearestExpiryFormatted == "N/A") "No Date" else model.nearestExpiryFormatted,
                backgroundColor = LightBeigeField,
                textColor = Color.DarkGray,
                modifier = Modifier.weight(1.2f)
            )

            // 4. Current stock (weight 0.8f)
            DataFieldBox(
                text = model.totalUnitFormatted(),
                backgroundColor = AccentBrown,
                textColor = Color.White,
                modifier = Modifier.weight(0.8f),
                centered = true,
                isBold = true
            )

            // 5. Actions (weight 1.5f)
            Row(
                modifier = Modifier.weight(1.5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButtons(
                    modifier = Modifier.weight(0.25f),
                    icon = Icons.Default.Remove,
                    onClick = { showDeleteBatch = true },
                    enabled = model.batch.isNotEmpty()
                )

                ActionButtons(
                    modifier = Modifier.weight(0.25f),
                    icon = Icons.Default.Add,
                    onClick = { showInsertBatch = true },
                    enabled = true
                )

                ViewMoreButtons(
                    modifier = Modifier.weight(0.5f),
                    onClick = { showItemDetail = true }
                )
            }
        }
    }

    /* Popups management stays here... */
    if (showItemDetail) {
        ItemDetailPopup(model = model, onDismiss = { showItemDetail = false }, onUpdateItem = itemViewModel::updateItem)
    }
    if (showInsertBatch) {
        BatchInsertionPopup(itemModel = model, onDismiss = { showInsertBatch = false })
    }
    if (showDeleteBatch) {
        BatchTargetedRemoval(threshold = model.item.subUnitThreshold, batch = model.batch, onDismiss = { showDeleteBatch = false })
    }
}

@Composable
fun DataFieldBox(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier,
    centered: Boolean = false,
    isBold: Boolean = false
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp),
        contentAlignment = if (centered) Alignment.Center else Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = GoogleSans,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            ),
            maxLines = 1
        )
    }
}

@Composable
fun ActionButtons(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier
) {
    IconButton(
        modifier = modifier.size(32.dp).background(Color.Transparent),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = if (enabled) Color.Black else Color.Gray)
    }
}

@Composable
fun ViewMoreButtons(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(ActionBrown)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Update",
            style = TextStyle(
                fontFamily = GoogleSans,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}