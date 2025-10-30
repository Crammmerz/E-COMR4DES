package com.android.inventorytracker.presentation.popup.item_detail

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.popup.item_detail.component.AddUnitButton
import com.android.inventorytracker.presentation.popup.item_detail.component.BatchExpirySection
import com.android.inventorytracker.presentation.popup.item_detail.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_detail.component.PhotoSelection
import com.android.inventorytracker.presentation.popup.item_detail.component.RemoveStockButton
import com.android.inventorytracker.presentation.shared.component.item_property_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.NameField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.ThresholdField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.component.primitive.DialogMockup

@Composable
fun ItemDetail(
    item: ItemEntity,
    batchExpiry: ItemBatchEntity?,
    onDismiss: () -> Unit,
    onSave: (ItemEntity) -> Unit
) {
    var name by rememberSaveable(item.id) { mutableStateOf(item.name) }
    var unitThreshold by rememberSaveable(item.id) { mutableIntStateOf(item.unitThreshold) }
    var subUnitThreshold by rememberSaveable(item.id) { mutableIntStateOf(item.subUnitThreshold) }
    var description by rememberSaveable(item.id) { mutableStateOf(item.description) }

    DialogHost (// TODO: DialogHost for App testing while DialogMockup for UI Preview Testing
        modifier = Modifier.fillMaxSize(0.9f),
        onDismissRequest = onDismiss
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
                HeaderSection()
                HorizontalDivider(Modifier.padding(vertical = 5.dp), color = Color.DarkGray, thickness = 2.dp)

                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column(Modifier.weight(0.45f)) {
                        PhotoSelection()

                        // wire NameField to local state
                        NameField(
                            name = name,
                            onNameChange = { name = it },
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 5.dp)) {
                            Column(Modifier.weight(1f)) {
                                ThresholdField(
                                    unitThreshold = unitThreshold,
                                    onThresholdChange = { unitThreshold = it }
                                )
                                Spacer(Modifier.weight(1f))
                                RemoveStockButton()
                            }
                            Column(Modifier.weight(1f)) {
                                SubUnitField(
                                    subUnitThreshold = subUnitThreshold,
                                    onSubUnitThresholdChange = { subUnitThreshold = it }
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

                        if (batchExpiry != null) {
                            BatchExpirySection(
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()) {
                            }
                        }
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
    ItemDetail(
        item = sampleItemEntity,
        batchExpiry = sampleExpiryBatch,
        onDismiss = noopOnDismiss,
        onSave = noopOnSave
    )
}