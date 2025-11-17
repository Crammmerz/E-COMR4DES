package com.android.inventorytracker.presentation.popup.item_detail.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.item_detail.ScreenMode
import com.android.inventorytracker.presentation.shared.component.item_property_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.NameField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost


@Composable
fun ItemDetailView(
    itemModel: ItemModel,
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
    onUpdateBatch: (List<ItemBatchEntity>, Int, Int) -> Unit,
    setMode: (ScreenMode) -> Unit
){
    var name by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.name) }
    var unitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.unitThreshold) }
    var subUnitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.subUnitThreshold) }
    var description by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.description) }
    val context = LocalContext.current

    // TODO: DialogHost for App testing while DialogMockup for UI Preview Testing
    DialogHost(
        modifier = Modifier.fillMaxSize(0.9f),
        useImePadding = true,
        onDismissRequest = {
            val updatedItem = itemModel.item.copy(
                name = name,
                unitThreshold = unitThreshold,
                subUnitThreshold = subUnitThreshold,
                description = description
            )

            if (itemModel.item != updatedItem) {
                if (itemModel.item.subUnitThreshold != updatedItem.subUnitThreshold) {
                    onUpdateBatch(
                        itemModel.batch,
                        itemModel.item.subUnitThreshold,
                        updatedItem.subUnitThreshold
                    )
                }
                onUpdateItem(updatedItem)
                Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show()
            }

            onDismiss()
        },
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                HeaderSection(itemModel)
                HorizontalDivider(
                    Modifier.padding(vertical = 5.dp),
                    color = Color.DarkGray,
                    thickness = 2.dp
                )

                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column(Modifier.weight(0.45f)) {
                        PhotoSelection()

                        NameField(
                            name = name,
                            onNameChange = { name = it },
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(top = 5.dp)
                        ) {
                            Column(Modifier.weight(1f)) {
                                UnitField(
                                    unit = unitThreshold,
                                    onUnitChange = { unitThreshold = it }
                                )
                                Spacer(Modifier.weight(1f))
                                RemoveStockButton(onClick = { setMode(ScreenMode.DELETE_BATCH) })
                            }
                            Column(Modifier.weight(1f)) {
                                SubUnitField(
                                    subUnit = subUnitThreshold,
                                    onSubUnitChange = { subUnitThreshold = it }
                                )
                                Spacer(Modifier.weight(1f))
                                BatchInsertionButton(onClick = { setMode(ScreenMode.ADD_BATCH) })
                            }
                        }
                    }

                    Column(Modifier.weight(0.55f)) {
                        DescriptionField(
                            description = description,
                            onDescriptionChange = { description = it },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(Modifier.height(8.dp))

                        BatchExpirySection(
                            model = itemModel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}