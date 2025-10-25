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
fun ItemDetail(item: ItemEntity){
    var name by rememberSaveable { mutableStateOf(item.name) }
    var unitThreshold by rememberSaveable { mutableIntStateOf(item.unitThreshold) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(item.subUnitThreshold) }
    var description by rememberSaveable { mutableStateOf(item.description) }
    DialogHost(Modifier.fillMaxSize(0.9f),{}) {
        Box(Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
                HeaderSection()
                HorizontalDivider(Modifier.padding(vertical = 5.dp),color = Color.DarkGray, thickness = 2.dp)
                Row (Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column (Modifier.weight(0.45f)) {
                        PhotoSelection()
                        NameField(name, {})//TODO:
                        Row (horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 5.dp)){
                            Column (Modifier.weight(1f)){
                                ThresholdField(unitThreshold, {unitThreshold = it}) //TODO:
                                Spacer(Modifier.weight(1f))
                                RemoveStockButton()
                            }
                            Column (Modifier.weight(1f)) {
                                SubUnitField(subUnitThreshold,  { subUnitThreshold = it })//TODO:
                                Spacer(Modifier.weight(1f))
                                AddUnitButton()
                            }
                        }
                    }
                    Column (Modifier.weight(0.55f)) {
                        DescriptionField(description, {description = it},Modifier.weight(1f))//TODO:
                        Spacer(Modifier.weight(0.1f))
                        BatchExpirySection(Modifier.weight(1f))//TODO:
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "The Preview",
    device = "spec:width=960dp,height=600dp,dpi=240,isRound=false,orientation=landscape"
)
@Composable
fun DialogPreview(){
    val item = ItemEntity(
        id = 0,
        imageUri = "content://media/external/images/media/12345",
        name = "Arabica Beans",
        unitThreshold = 10,
        subUnitThreshold = 5,
        description = "Premium roasted coffee beans"
    )
    ItemDetail(item)
}