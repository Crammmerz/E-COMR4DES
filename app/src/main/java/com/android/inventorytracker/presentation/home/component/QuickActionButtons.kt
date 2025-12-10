package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.popup.batch_group_insertion.BatchGroupInsertionPopup
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Ochre

@Composable
fun QuickActions(itemViewModel: ItemViewModel = hiltViewModel(),) {
    val model by itemViewModel.itemModelList.collectAsState(initial = emptyList())
    var showAddStock by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        Buttons("Add Stock", Modifier.weight(1f)){ showAddStock = true}
        Buttons("Deduct Stock", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
//        Buttons("Place Holder", Modifier.weight(1f)){}
    }
    if(showAddStock){
        BatchGroupInsertionPopup(model = model, onDismiss = { showAddStock = false })
    }
}

@Composable
fun Buttons(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CenterButton(
        modifier = modifier
            .fillMaxHeight(),
        label = label,
        bgColor = Color.White,
        contentColor = Ochre,
        onClick = onClick
    )
}