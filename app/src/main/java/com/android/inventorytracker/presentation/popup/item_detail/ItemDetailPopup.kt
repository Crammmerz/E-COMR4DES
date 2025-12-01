package com.android.inventorytracker.presentation.popup.item_detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.item_detail.component.BatchExpirySection
import com.android.inventorytracker.presentation.popup.item_detail.component.PhotoSelection
import com.android.inventorytracker.presentation.shared.component.input_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun ItemDetailPopup(
    itemModel: ItemModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
    onUpdateBatch: (List<ItemBatchEntity>, Int, Int) -> Unit
){
    var name by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.name) }
    var unitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.unitThreshold) }
    var subUnitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.subUnitThreshold) }
    var expiryThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.expiryThreshold) }
    var description by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.description) }

    val context = LocalContext.current

    DialogHost(
        modifier = Modifier
            .fillMaxSize(0.85f),
        useImePadding = true,
        onDismissRequest = {
            val updatedItem = itemModel.item.copy(
                name = name,
                unitThreshold = unitThreshold,
                subUnitThreshold = subUnitThreshold,
                expiryThreshold = expiryThreshold,
                description = description
            )
            if (itemModel.item != updatedItem && loginViewModel.userRole == UserRole.ADMIN) {
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
            if(itemModel.item != updatedItem && loginViewModel.userRole == UserRole.STAFF){
                Toast.makeText(context, "You are not authorized to edit this item", Toast.LENGTH_SHORT).show()
            }
            onDismiss()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(Modifier.weight(0.40f)) {
                    PhotoSelection()

                    StringField(
                        text = name,
                        onTextChange = { name = it },
                        header = "Item Name"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(Modifier.weight(1f)) {
                            IntField(
                                num = unitThreshold,
                                onNumChange = { unitThreshold = it },
                                header = "Unit"
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            IntField(
                                num = subUnitThreshold,
                                onNumChange = { subUnitThreshold = it },
                                header = "Sub Unit"
                            )
                        }
                    }
                    IntField(
                        num = expiryThreshold,
                        onNumChange = { expiryThreshold = it },
                        header = "Expiry Threshold"
                    )
                }

                Column(Modifier.weight(0.60f)) {
                    DescriptionField(
                        description = description,
                        onDescriptionChange = { description = it },
                        modifier = Modifier.weight(0.45f)
                    )

                    Spacer(Modifier.height(10.dp))

                    BatchExpirySection(
                        model = itemModel,
                        modifier = Modifier.weight(0.55f)
                    )
                }
            }
        }
    }
}
