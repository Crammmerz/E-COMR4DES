package com.android.inventorytracker.presentation.inventory.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.android.inventorytracker.ui.theme.LightSand



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
            .height(60.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp))
            .padding(horizontal = 5.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
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
                    .size(64.dp)
                    .padding(horizontal = 10.dp),
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.outline_add_photo_alternate_24),
                contentDescription = "Placeholder image",
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .size(64.dp)
                    .padding(horizontal = 10.dp),
            )
        }
        ItemText(text = model.item.name, modifier = Modifier.weight(0.75f))
        ItemText(
            text = model.nearestExpiryFormatted,
            modifier = Modifier.weight(0.5f),
            color = model.expiryColor
        )
        ItemText(
            text = model.totalUnitFormatted(),
            modifier = Modifier
                .weight(0.5f),
            textAlign = TextAlign.Center,
            color = model.stockColor,
            )
        ItemButton(
            modifier =  Modifier.weight(0.25f),
            enabled = model.batch.isNotEmpty(),
            onClick = { showDeleteBatch = true },
            text = "-"
        )
        ItemButton (
            modifier =  Modifier.weight(0.25f),
            onClick = { showInsertBatch = true },
            text = "+"
        )
        ItemButton("View More", modifier = Modifier.weight(0.5f)) { showItemDetail = true }
    }

    if (showItemDetail) {
        ItemDetailPopup(
            itemModel = model,
            onDismiss = { showItemDetail = false },
            onUpdateItem = itemViewModel::updateItem,
            onUpdateBatch = batchViewModel::onConvertBatch
        )
    }

    if(showInsertBatch){
        BatchInsertionPopup(
            itemModel = model,
            onDismiss = { showInsertBatch = false },
        )
    }

    if(showDeleteBatch){
        BatchTargetedRemoval(
            threshold = model.item.subUnitThreshold,
            batch = model.batch,
            onDismiss = { showDeleteBatch = false },
        )
    }
}

@Composable
fun ItemText(
    text: String,
    color: Color = Color.Gray,
    modifier: Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        textAlign = textAlign,
        maxLines = 1,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        modifier = modifier
            .background(LightSand, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .border(if(color == Color.Gray)1.dp else 2.dp, color = color, RoundedCornerShape(5.dp))
            .padding(horizontal = 5.dp, vertical = 7.dp)
    )
}

