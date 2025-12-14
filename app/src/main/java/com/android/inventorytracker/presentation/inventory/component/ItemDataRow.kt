package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

// Matches Image 1 input background
private val TextFieldBackgroundColor = Color(0xFFF8F8F8)

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

    Row(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(5.dp))
            .background(Color.White)
            .clip(RoundedCornerShape(5.dp))
            .padding(horizontal = 5.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        // Item Image
        Image(
            painter = if (model.item.imageUri != null)
                rememberAsyncImagePainter(
                    model = model.item.imageUri,
                    placeholder = painterResource(R.drawable.outline_add_photo_alternate_24),
                    error = painterResource(R.drawable.outline_add_photo_alternate_24)
                )
            else painterResource(R.drawable.outline_add_photo_alternate_24),
            contentDescription = "Item image",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(5.dp))
                .padding(horizontal = 10.dp)
        )

        // Item Name
        ItemText(
            text = model.item.name,
            modifier = Modifier.weight(0.75f)
        )

        // Expiry
        ItemText(
            text = model.nearestExpiryFormatted,
            modifier = Modifier.weight(0.5f),
            color = model.expiryColor
        )

        // Unit
        ItemText(
            text = model.totalUnitFormatted(),
            modifier = Modifier.weight(0.5f),
            color = model.stockColor,
            textAlign = TextAlign.Center
        )

        // Minus
        ItemButton(
            text = "-",
            enabled = model.batch.isNotEmpty(),
            modifier = Modifier.weight(0.25f)
        ) {
            showDeleteBatch = true
        }

        // Plus
        ItemButton(
            text = "+",
            modifier = Modifier.weight(0.25f)
        ) {
            showInsertBatch = true
        }

        // View More
        ItemButton(
            text = "View More",
            modifier = Modifier.weight(0.5f)
        ) {
            showItemDetail = true
        }
    }

    if (showItemDetail) {
        ItemDetailPopup(
            itemModel = model,
            onDismiss = { showItemDetail = false },
            onUpdateItem = itemViewModel::updateItem,
            onUpdateBatch = batchViewModel::onConvertBatch
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

@Composable
fun ItemText(
    text: String,
    modifier: Modifier,
    color: Color = Color.Gray,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign,
        modifier = modifier
            .background(TextFieldBackgroundColor, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = if (color == Color.Gray) 0.dp else 2.dp,
                color = color,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 5.dp, vertical = 7.dp)
    )
}
