package com.android.inventorytracker.presentation.inventory.component

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.R
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.add_new_batch.InsertBatchPopup
import com.android.inventorytracker.presentation.popup.delete_batch.DeleteBatchPopup
import com.android.inventorytracker.presentation.popup.item_detail.ItemDetailPopup
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.LightSand

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemDataRow(
    itemModel: ItemModel,
    itemViewModel: ItemViewModel,
    batchViewModel: BatchViewModel,
    modifier: Modifier = Modifier
) {
    var showItemDetail by rememberSaveable { mutableStateOf(false) }
    var showInsertBatch by rememberSaveable { mutableStateOf(false) }
    var showDeleteBatch by rememberSaveable { mutableStateOf(false) }

    val unit = batchViewModel.unit
    val subUnit = batchViewModel.subUnit

    val totalUnit = itemModel.totalUnit
    val threshold = itemModel.item.unitThreshold
    val darkRed = Color(0xFF8B0000)

    val stockColor = when {//TODO: Adjust Colors
        totalUnit.toInt() == 0 -> Color.DarkGray
        totalUnit <= threshold * 0.2f -> darkRed
        else -> LightSand
    }

    Row(
        modifier = modifier
            .height(75.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp))
            .padding(horizontal = 15.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.haahahaahha),
            contentDescription = "Placeholder image",
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .size(64.dp)
        )
        Spacer(Modifier.weight(0.1f))
        ItemText(itemModel.item.name, Modifier.weight(1f))
        ItemText(itemModel.nearestExpiryFormatted, Modifier.weight(0.75f))
        ItemText(itemModel.totalUnitFormatted, Modifier.weight(0.5f).background(stockColor, shape = RoundedCornerShape(5.dp)), TextAlign.Center)
        ItemButton("-", Modifier.weight(0.25f)) { showDeleteBatch = true }
        ItemButton("+", Modifier.weight(0.25f)) { showInsertBatch = true }
        ItemButton("View More", Modifier.weight(0.5f)) { showItemDetail = true }
    }

    if (showItemDetail) {
        ItemDetailPopup(
            itemModel = itemModel,
            onDismiss = { showItemDetail = false },
            onUpdate = itemViewModel::updateItem
        )
    }

    if(showInsertBatch){
        LaunchedEffect(true) {
            batchViewModel.unitReset()
        }
        InsertBatchPopup(
            itemModel = itemModel,
            unit = unit,
            subUnit = subUnit,
            onUnitChange = { batchViewModel.onUnitChange(it, itemModel.item.subUnitThreshold) },
            onSubUnitChange = { batchViewModel.onSubUnitChange(it, itemModel.item.subUnitThreshold) },
            onDismiss = { showInsertBatch = false },
            onStore = batchViewModel::storeBatch
        )
    }

    if(showDeleteBatch){
        LaunchedEffect(true) {
            batchViewModel.unitReset()
        }
        DeleteBatchPopup(
            totalSubUnit = itemModel.totalSubUnit,
            batch = itemModel.batch,
            unit = unit,
            subUnit = subUnit,
            onUnitChange = { batchViewModel.onUnitChange(it, itemModel.item.subUnitThreshold) },
            onSubUnitChange = { batchViewModel.onSubUnitChange(it, itemModel.item.subUnitThreshold) },
            onDismiss = { showDeleteBatch = false },
            onDelete = batchViewModel::deleteBatch
        )
    }
}

@Composable
fun ItemText(
    text: String,
    modifier: Modifier = Modifier.background(LightSand, shape = RoundedCornerShape(5.dp)),
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        textAlign = textAlign,
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
            .padding(horizontal = 7.dp, vertical = 7.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = "spec:width=960dp,height=600dp,dpi=240,isRound=false,orientation=landscape")
@Composable
fun ItemOverviewSectionPreview() {
    val sampleModel = ItemModel(
        item = com.android.inventorytracker.data.local.entities.ItemEntity(
            id = 1,
            imageUri = null,
            name = "Coffee Beans",
            unitThreshold = 10,
            subUnitThreshold = 20,
            description = ""
        ),
        batch = listOf(
            com.android.inventorytracker.data.local.entities.ItemBatchEntity(itemId = 1, expiryDate = "2025-11-01", subUnit = 20),
            com.android.inventorytracker.data.local.entities.ItemBatchEntity(itemId = 1, expiryDate = "2025-10-31", subUnit = 15)
        )
    )
//
//    ItemDataRow(
//        itemModel = sampleModel,
//        showItemDetail = false,
//        onShowItemDetailChange = {},
//        itemViewModel = ,
//        batchViewModel = ,
//    )
}
