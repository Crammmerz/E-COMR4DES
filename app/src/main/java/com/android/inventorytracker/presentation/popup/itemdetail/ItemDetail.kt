package com.android.inventorytracker.presentation.popup.itemdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.component.primitive.LeftColumn
import com.android.inventorytracker.presentation.popup.itemdetail.component.AddStockButton
import com.android.inventorytracker.presentation.popup.itemdetail.component.BatchExpirySection
import com.android.inventorytracker.presentation.popup.itemdetail.component.DescriptionField
import com.android.inventorytracker.presentation.popup.itemdetail.component.HeaderSection
import com.android.inventorytracker.presentation.popup.itemdetail.component.NameField
import com.android.inventorytracker.presentation.popup.itemdetail.component.PhotoSelection
import com.android.inventorytracker.presentation.popup.itemdetail.component.RemoveStockButton
import com.android.inventorytracker.presentation.popup.itemdetail.component.SubUnitField
import com.android.inventorytracker.presentation.popup.itemdetail.component.ThresholdField

@Composable
fun ItemDetail(){
    DialogHost(Modifier.fillMaxSize(0.9f),{}) {
    //DialogMockup (Modifier.fillMaxSize(0.9f)) {
        Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
            HeaderSection()
            HorizontalDivider(Modifier.padding(vertical = 5.dp), thickness = 2.dp)
            Row (Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                LeftColumn (Modifier.weight(0.45f)) {
                    PhotoSelection()
                    NameField()
                    Row (horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 5.dp)){
                        LeftColumn (Modifier.weight(1f)){
                            ThresholdField()
                            Spacer(Modifier.weight(1f))
                            RemoveStockButton()
                        }
                        LeftColumn (Modifier.weight(1f)) {
                            SubUnitField()
                            Spacer(Modifier.weight(1f))
                            AddStockButton()
                        }
                    }
                }
                LeftColumn (Modifier.weight(0.55f)) {
                    DescriptionField(Modifier.weight(1f))
                    Spacer(Modifier.weight(0.1f))
                    BatchExpirySection(Modifier.weight(1f))
                }
            }
        }
    }
}