package com.android.inventorytracker.presentation.popup.item_detail

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.item_detail.component.AddUnitButton
import com.android.inventorytracker.presentation.popup.item_detail.component.BatchExpirySection
import com.android.inventorytracker.presentation.popup.item_detail.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_detail.component.PhotoSelection
import com.android.inventorytracker.presentation.popup.item_detail.component.RemoveStockButton
import com.android.inventorytracker.presentation.shared.component.item_property_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.NameField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun ItemDetailPopup(
    itemModel: ItemModel,
    onDismiss: () -> Unit,
    onUpdate: (ItemEntity) -> Unit
) {
    var name by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.name) }
    var unitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.unitThreshold) }
    var subUnitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.subUnitThreshold) }
    var description by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.description) }
    val context = LocalContext.current

    DialogHost (// TODO: DialogHost for App testing while DialogMockup for UI Preview Testing
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
                onUpdate(updatedItem)
                Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show()
            }

            onDismiss()
        },
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)) {
                HeaderSection(itemModel)
                HorizontalDivider(Modifier.padding(vertical = 5.dp), color = Color.DarkGray, thickness = 2.dp)

                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column(Modifier.weight(0.45f)) {
                        PhotoSelection()

                        NameField(
                            name = name,
                            onNameChange = { name = it },
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 5.dp)) {
                            Column(Modifier.weight(1f)) {
                                UnitField(
                                    unit = unitThreshold,
                                    onUnitChange = { unitThreshold = it }
                                )
                                Spacer(Modifier.weight(1f))
                                RemoveStockButton()
                            }
                            Column(Modifier.weight(1f)) {
                                SubUnitField(
                                    subUnit = subUnitThreshold,
                                    onSubUnitChange = { subUnitThreshold = it }
                                )
                                Spacer(Modifier.weight(1f))
                                AddUnitButton()
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

val sampleItemEntity = ItemEntity(
    id = 1,
    imageUri = null,
    name = "Arabica Beans",
    unitThreshold = 10,
    subUnitThreshold = 5,
    description = "Premium roasted coffee beans"
)

// sample ItemModel (nearestExpiryDate as String; change type if yours is LocalDate)


// sample ExpiryBatchEntity (adapt fields to your actual entity)
val sampleExpiryBatch = ItemBatchEntity(
    id = 100,
    itemId = sampleItemEntity.id,
    expiryDate = "2025-12-31",
    subUnit = 20
)

// noop callbacks for preview/test
val noopOnDismiss: () -> Unit = {}
val noopOnSave: (ItemEntity) -> Unit = { /* capture or assert in tests */ }

@Preview(
    showBackground = true,
    name = "The Preview",
    device = "spec:width=960dp,height=600dp,dpi=240,isRound=false,orientation=landscape"
)
@Composable
fun Preview() {
}