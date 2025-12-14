package com.android.inventorytracker.presentation.popup.item_removal.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.data.model.ItemModel

private val CoffeeBrown = Color(0xFF5D4037)
private val MutedText = Color(0xFF7A6A5F)
private val CardBorder = Color(0xFFE0D6CC)

@Composable
fun InventoryItem(
    itemModel: ItemModel,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )

        Image(
            painter = rememberAsyncImagePainter(itemModel.item.imageUri),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column {
            Text(
                text = itemModel.item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CoffeeBrown
            )
            Text(
                text = "Quantity: ${itemModel.totalUnitFormatted()} • Expiry: ${itemModel.nearestExpiryFormatted}",
                fontSize = 12.sp,
                color = MutedText
            )
        }
    }
}
