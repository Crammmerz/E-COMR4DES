package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.android.inventorytracker.presentation.popup.batch_group_removal.BatchGroupRemovalPopup
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun QuickActions(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    val model by itemViewModel.itemModelList.collectAsState(initial = emptyList())
    // Changed state variable names from Stock to Item
    var showAddItem by rememberSaveable { mutableStateOf(false) } // CHANGED from showAddStock
    var showDeductItem by rememberSaveable { mutableStateOf(false) } // CHANGED from showRemoveStock

    Row(
        modifier = modifier
            .height(60.dp)
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start)
    ) {

        QuickActionButton(
            label = "Add Item",
            modifier = Modifier.weight(1f),
            bgColor = Palette.AccentBeigePrimary, // Deep Beige
            contentColor = Palette.PureWhite,   // White text
            onClick = { showAddItem = true } // CHANGED variable name
        )

        QuickActionButton(
            label = "Deduct Item",
            modifier = Modifier.weight(1f),
            bgColor = Palette.AccentBeigeLight, // Light Beige/Neutral
            contentColor = Palette.DarkBeigeText, // Dark Beige text for contrast
            onClick = { showDeductItem = true } // CHANGED variable name
        )

        // Button 3: New Item (Tertiary Action: White Background)
        QuickActionButton(
            label = "New Item",
            modifier = Modifier.weight(1f),
            bgColor = Palette.PureWhite, // Pure white background
            contentColor = Palette.DarkBeigeText, // Dark Beige text
            onClick = { /* Placeholder action */ }
        )
    }

    if(showAddItem){ // CHANGED variable name
        BatchGroupInsertionPopup(model = model, onDismiss = { showAddItem = false }) // CHANGED variable name
    }
    if(showDeductItem){ // CHANGED variable name
        BatchGroupRemovalPopup(model = model, onDismiss = { showDeductItem = false }) // CHANGED variable name
    }
}

// QuickActionButton implementation remains the same
@Composable
fun QuickActionButton(
    label: String,
    modifier: Modifier = Modifier,
    bgColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    CenterButton(
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 16.dp),
        label = label,
        bgColor = bgColor,
        contentColor = contentColor,
        onClick = onClick,
    )
}