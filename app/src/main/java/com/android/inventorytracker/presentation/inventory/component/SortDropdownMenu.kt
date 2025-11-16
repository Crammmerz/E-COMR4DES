package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.SortBy
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun SortDropdownMenu(itemViewModel: ItemViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Name (A-Z)") },
                onClick = { itemViewModel.setSort(SortBy.NAME_ASC) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Name (Z-A)") },
                onClick = { itemViewModel.setSort(SortBy.NAME_DESC) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Expiry (Soon)") },
                onClick = { itemViewModel.setSort(SortBy.EXPIRY_SOONEST) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Stock (Low-High)") },
                onClick = { itemViewModel.setSort(SortBy.STOCK_LOW_HIGH) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Stock (High-Low)") },
                onClick = { itemViewModel.setSort(SortBy.STOCK_HIGH_LOW) }
            )
        }
    }
}