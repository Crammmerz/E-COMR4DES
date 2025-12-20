package com.android.inventorytracker.presentation.popup.csv_removal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.popup.csv_removal.component.DataRow
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CsvViewModel
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel

@Composable
fun CsvRemovalPopup(
    csvViewModel: CsvViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
){
    val model = csvViewModel.model.collectAsState(initial = emptyList())
    val data = csvViewModel.data.observeAsState(emptyList())
    val dropped = csvViewModel.droppedCount.observeAsState()
    var validityMap by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var valid by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun onConfirm(){
        valid = data.value.all { row ->
            validityMap[row.id] == true
        }

        if (valid) {
            data.value.forEach { row ->
                val matchedModel = model.value.firstOrNull { it.item.id == row.id }
                matchedModel?.let {
                    batchViewModel.onDeductStock(it.batch, row.subunit)
                }
            }
            csvViewModel.clearData()
            onDismiss()
        } else {
            Toast.makeText(
                context,
                "Oops! Looks like something’s missing — select an item or fill in the required fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    DialogHost(
        modifier = Modifier
            .size(600.dp),
        onDismissRequest = {
            csvViewModel.clearData()
            onDismiss()
        },
        useImePadding = true
    ) {
        Column (Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            Text("Set Values")
            if(dropped.value != null && dropped.value != 0) {
                Text( "Dropped ${dropped.value} items")
            }
            if(data.value.isNotEmpty()){
                LazyColumn (
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White)
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ){
                    items(
                        items = data.value,
                        key = { row -> row.id }
                    ) { row ->
                        val matchedModel = model.value.firstOrNull { it.item.id == row.id }
                        if (matchedModel != null) {
                            DataRow(
                                model = matchedModel,
                                subUnit = row.subunit,
                                onUpdate = csvViewModel::updateData,
                                onValidityChange = { valid ->
                                    validityMap = validityMap + (matchedModel.item.id to valid)
                                }
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data has been read")
                }
            }

            CancelButton {
                csvViewModel.clearData()
                onDismiss()
            }

            ConfirmButton("Deduct", {
                onConfirm()
            })
        }
    }
}