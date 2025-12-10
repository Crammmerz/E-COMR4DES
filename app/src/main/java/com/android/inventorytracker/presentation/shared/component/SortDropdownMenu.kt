package com.android.inventorytracker.presentation.shared.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.SortBy
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun SortDropdownMenu(itemViewModel: ItemViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val sortBy by itemViewModel.sortBy.collectAsState()
    var text by remember { mutableStateOf("") }
    text = when (sortBy){
        SortBy.NAME_ASC -> "Name (A-Z)"
        SortBy.NAME_DESC -> "Name (Z-A)"
        SortBy.EXPIRY_ASCENDING -> "Expiry (Ascending)"
        SortBy.STOCK_LOW -> "Stock (Less Than 20%)"
        SortBy.STOCK_LOW_HIGH -> "Stock (Low-High)"
        SortBy.STOCK_HIGH_LOW -> "Stock (High-Low)"
    }
    Box (contentAlignment = Alignment.Center){
        TextButton (
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp))
                .width(175.dp)
                .height(40.dp),
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            when (expanded) {
                true -> Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "More options",
                )
                false -> Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "More options",
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.align(Alignment.TopStart)
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
                text = { Text("Expiry (Ascending)") },
                onClick = { itemViewModel.setSort(SortBy.EXPIRY_ASCENDING) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Stock (Less Than 20%)") },
                onClick = { itemViewModel.setSort(SortBy.STOCK_LOW) }
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