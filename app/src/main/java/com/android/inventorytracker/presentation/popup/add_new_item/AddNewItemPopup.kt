package com.android.inventorytracker.presentation.popup.add_new_item

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.popup.add_new_item.component.AddItemButton
import com.android.inventorytracker.presentation.popup.add_new_item.component.CancelButton
import com.android.inventorytracker.presentation.popup.add_new_item.component.Header
import com.android.inventorytracker.presentation.shared.component.item_property_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.NameField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.ThresholdField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.component.primitive.DialogMockup
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun AddNewItemPopup(showPopup: MutableState<Boolean>, viewModel: ItemViewModel){
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("Enter item name") }
    var unitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var description by rememberSaveable { mutableStateOf("Enter item description") }

    DialogHost(
        modifier = Modifier.fillMaxWidth(0.4f)
            .fillMaxHeight(0.8f),
        onDismissRequest = { showPopup.value = false }
    ) {
//  DialogMockup { TODO: This is for Testing Dialogues UI
        Box {
            Column(Modifier.fillMaxSize().padding(5.dp)) {
                Header()
                NameField(name, {name = it}, Modifier.padding(top = 10.dp))
                Row (Modifier.padding(top = 5.dp), Arrangement.spacedBy(15.dp),){
                    Column (Modifier.weight(1f)){
                        ThresholdField(unitThreshold, {unitThreshold = it}, Modifier.padding(top = 10.dp)) //TODO:
                    }
                    Column (Modifier.weight(1f)) {
                        SubUnitField(subUnitThreshold,  { subUnitThreshold = it }, Modifier.padding(vertical = 10.dp))//TODO:
                    }
                }
                DescriptionField(description, {description = it}, Modifier.fillMaxHeight(0.5f))
            }
            Row(modifier = Modifier.align(Alignment.BottomEnd),horizontalArrangement = Arrangement.spacedBy(10.dp)){
                CancelButton(onClick = { showPopup.value = false })
                AddItemButton({
                    if(viewModel.isValidItem(name, unitThreshold, subUnitThreshold)){
                        val item = ItemEntity(
                            imageUri = null,
                            name = name,
                            unitThreshold = unitThreshold,
                            subUnitThreshold = subUnitThreshold,
                            description = description
                        )
                        viewModel.insertItem(item)
                        Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Item not added", Toast.LENGTH_SHORT).show()
                    }
                })
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
        id = 0, // Let Room auto-generate this
        imageUri = "content://media/external/images/media/12345",
        name = "Arabica Beans",
        unitThreshold = 10,
        subUnitThreshold = 5,
        description = "Premium roasted coffee beans"
    )
    val showPopup = rememberSaveable { mutableStateOf(true) }
//    AddNewItemPopup(showPopup)
}