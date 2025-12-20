package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_insertion.BatchInsertionPopup
import com.android.inventorytracker.presentation.popup.batch_targeted_removal.BatchTargetedRemoval
import com.android.inventorytracker.presentation.popup.item_detail.ItemDetailPopup
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

val SurfaceWhite = Color.White
val LightBeigeField = Color(0xFFF2ECE6)
val AccentBrown = Color(0xFFC19A6B)
val ActionBrown = Color(0xFF8D6E63)
val BorderColor = Color(0xFFE0E0E0)

@Composable
fun ItemDataRow(
    model: ItemModel,
    itemViewModel: ItemViewModel,
    batchViewModel: BatchViewModel,
    modifier: Modifier = Modifier
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

            // 1. Image
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

            // 2. Item Name
            DataFieldBox(
                text = model.item.name,
                backgroundColor = LightBeigeField,
                textColor = Color.Black,
                modifier = Modifier.weight(3.0f)
            )

            // 3. Expiry
            DataFieldBox(
                text = if (model.nearestExpiryFormatted == "N/A") "No Date"
                else model.nearestExpiryFormatted,
                backgroundColor = LightBeigeField,
                textColor = Color.DarkGray,
                modifier = Modifier.weight(1.2f)
            )

            // 4. Current stock
            DataFieldBox(
                text = model.totalUnitFormatted(),
                backgroundColor = AccentBrown,
                textColor = Color.White,
                modifier = Modifier.weight(0.8f),
                centered = true,
                isBold = true
            )

            // 5. Actions
            Row(
                modifier = Modifier.weight(1.5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ActionButton(
                    modifier = Modifier.weight(0.25f),
                    icon = Icons.Default.Remove,
                    onClick = { showDeleteBatch = true },
                    enabled = model.batch.isNotEmpty()
                )

                ActionButton(
                    modifier = Modifier.weight(0.25f),
                    icon = Icons.Default.Add,
                    onClick = { showInsertBatch = true },
                    enabled = true
                )

                ViewMoreButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { showItemDetail = true }
                )
            }
        }
    }

    /* ---------------- Popups ---------------- */

    if (showItemDetail) {
        ItemDetailPopup(
            itemModel = model,
            onDismiss = { showItemDetail = false },
            onUpdateItem = itemViewModel::updateItem
        )
    }

    if (showInsertBatch) {
        BatchInsertionPopup(
            itemModel = model,
            onDismiss = { showInsertBatch = false }
        )
    }

    if (showDeleteBatch) {
        BatchTargetedRemoval(
            threshold = model.item.subUnitThreshold,
            batch = model.batch,
            onDismiss = { showDeleteBatch = false }
        )
    }
}

/* ---------------- Helpers ---------------- */

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
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier
) {
    IconButton(
        modifier = modifier.background(Color.White),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}

@Composable
fun ViewMoreButton(
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
            text = "View",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
