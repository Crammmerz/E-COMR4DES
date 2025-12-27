package com.android.inventorytracker.presentation.popup.batch_group_removal.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.android.inventorytracker.data.model.RemoveBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange

@Composable
fun ItemRemovalRow(
    modifier: Modifier = Modifier,
    model: ItemModel,
    isPersistent: Boolean,
    onValueChange: (RemoveBatch) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }
    var valid by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(unit, subUnit) {
        if (isPersistent) onValidityChange(valid)
        if (valid && isPersistent) {
            onValueChange(
                RemoveBatch(
                    itemId = model.item.id,
                    itemName = model.item.name,
                    batches = model.batch,
                    unit = unit,
                    subunit = subUnit
                )
            )
        }
    }

    LaunchedEffect(unit) {
        onUnitChange(unit, model.item.subUnitThreshold, { subUnit = it })
    }

    LaunchedEffect(subUnit) {
        onSubUnitChange(subUnit, model.item.subUnitThreshold, { unit = it })
    }


    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isPersistent) Color.White else Color.Transparent,
        border = if (isPersistent) androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)) else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPersistent,
                    onCheckedChange = { itemViewModel.togglePersistence(model.item.id, it) },
                    colors = CheckboxDefaults.colors(checkedColor = Palette.ButtonDarkBrown)
                )

                // Square Rounded Image
                AsyncImage(
                    model = model.item.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f)),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(
                        text = model.item.name,
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Palette.ButtonDarkBrown
                        )
                    )
                    Text(
                        text = "Stock: ${model.totalUnitFormatted()}",
                        style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray)
                    )
                }

                if(isPersistent) {
                    Icon(
                        imageVector = if(valid) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if(valid) Color(0xFF4CAF50) else Color(0xFFE57373),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (isPersistent) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FloatField(
                        modifier = Modifier.weight(1f),
                        label = "Deduct Unit",
                        placeholder = "0",
                        value = unit,
                        valueRange = 0f..model.totalUnit().toFloat(),
                        onValueChange = { unit = it },
                        onValidityChange = { valid = it && (unit > 0 || subUnit > 0) && (subUnit <= model.totalSubUnit()) },
                        onDone = null
                    )
                    IntField(
                        modifier = Modifier.weight(1f),
                        label = "Deduct Sub Unit",
                        placeholder = "0",
                        doClear = true,
                        value = subUnit,
                        valueRange = 1..model.totalSubUnit(),
                        onValueChange = { subUnit = it },
                        onValidityChange = { valid = it && (unit > 0 || subUnit > 0) && (subUnit <= model.totalSubUnit()) },
                        onDone = null
                    )
                }
            }
        }
    }
}